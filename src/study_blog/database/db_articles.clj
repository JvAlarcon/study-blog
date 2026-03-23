(ns study-blog.database.db-articles
  (:require [datomic.api :as d]
            [study-blog.database.core :as db-core])
  (:import [java.time LocalDateTime]
           [java.time.format DateTimeFormatter]))

(def dt-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss"))

(defn create-article
  "Store an article attached to an author inside datomic.
  It will return the id stored in datomic."
  [title body author-email]
  (let [temp-id "new-article"
        dt-now (.format (LocalDateTime/now) dt-formatter)
        result @(d/transact
                 (db-core/get-conn)
                 [{:db/id temp-id
                   :article/title title
                   :article/body body
                   :article/created-at dt-now
                   :article/author [:author/email author-email]}])]
    (get-in result [:tempids temp-id])))

(defn format-date
  "Validates and normalize the input string to ensure correct format for date.
  The accepted pattern is `yyyy-MM-dd HH:mm:ss`"
  [date-str]
  (->> date-str
       (LocalDateTime/parse dt-formatter)
       (.format dt-formatter)))

(defn list-articles
  "Lists all articles stored in datomic with optional filter.
  Options:
  - :author-name - filter by author name
  - :start-date - filter articles created after this date (pattern `yyyy-MM-dd HH:mm:ss`
  - :end-date - filter articles created before this date (pattern `yyyy-MM-dd HH:mm:ss`))
  "
  [& {:keys [author-name start-date end-date]}]
  (let [db (db-core/get-db)
        validated-start-date (some-> start-date format-date)
        validated-end-date (some-> end-date format-date)
        base-query '{:find [?e ?title ?body ?created-at ?author-name]
                     :keys [id title body created-at author-name]
                     :in [$]
                     :where [[?e :article/title ?title]
                             [?e :article/body ?body]
                             [?e :article/created-at ?created-at]
                             [?e :article/author ?author]
                             [?author :author/name ?author-name]]}
        query (cond-> base-query
                author-name (-> (update :in conj '?name)
                                (update :where conj '[?author :author/name ?name]))
                validated-start-date (-> (update :where conj '[(>= ?created-at ?start-date)])
                                         (update :in conj '?start-date))
                validated-end-date (-> (update :where conj '[(< ?created-at ?end-date)])
                                       (update :in conj '?end-date)))
        args (cond-> [db]
               author-name (conj author-name)
               validated-start-date (conj validated-start-date)
               validated-end-date (conj validated-end-date))]
    (apply d/q query args)))

(defn get-article
  "Given a id in string format, return a map with the data of the article"
  [str-id]
  (let [db (db-core/get-db)
        id (parse-long str-id)]
    (->
     (d/pull db '[:db/id
                  :article/title
                  :article/body
                  :article/created-at
                  {:article/author [:author/name]}] id)
     db-core/remove-ns-from-keys)))

(defn update-article
  "Updates the data of an article stored in datomic.
  Author can't be updated, only title and body of an article."
  [str-id title body]
  (let [id (parse-long str-id)]
    @(d/transact (db-core/get-conn) [{:db/id id
                                      :article/title title
                                      :article/body body}])
    id))

(defn delete-article
  "Given an article id, delete this article from datomic.
  It will return the previous id of the deleted article."
  [str-id]
  (let [id (parse-long str-id)]
    @(d/transact (db-core/get-conn) [[:db.fn/retractEntity id]])
    id))
