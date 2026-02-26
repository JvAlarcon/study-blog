(ns study-blog.database
  (:require [datomic.api :as d]
            [study-blog.schema :as schema]
            [study-blog.utils :as u])
  (:import [java.time LocalDateTime]
           [java.time.format DateTimeFormatter]))

(def db-connection-uri (u/require-env! "DB_URI"))
(def conn (atom nil))
(def dt-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss"))

(defn init! []
  (d/create-database db-connection-uri)
  (let [c (d/connect db-connection-uri)]
    (d/transact c schema/blog-schema)
    (reset! conn c)))

(defn create-article [title body]
  (let [temp-id "new-article"
        dt-now (.format (LocalDateTime/now) dt-formatter)
        result @(d/transact @conn [{:db/id temp-id
                                    :blog/title title
                                    :blog/body body
                                    :blog/created-at dt-now}])]
    (get-in result [:tempids temp-id])))

(def query-all-articles
  '[:find ?e ?title ?body ?created-at
    :keys id title body created-at
    :where [?e :blog/title ?title]
           [?e :blog/body ?body]
           [?e :blog/created-at ?created-at]])

(defn list-articles []
  (let [db (d/db @conn)]
    (d/q query-all-articles db)))

(defn remove-ns-from-keys [m]
  (update-keys m (comp keyword name)))

(defn get-article [str-id]
  (let [db (d/db @conn)
        id (parse-long str-id)]
    (->
     (d/pull db '[:db/id :blog/title :blog/body :blog/created-at] id)
     remove-ns-from-keys)))

(defn update-article [str-id title body]
  (let [id (parse-long str-id)]
    @(d/transact @conn [{:db/id id
                         :blog/title title
                         :blog/body body}])
    id))

(defn delete-article [str_id]
  (let [id (parse-long str_id)]
    @(d/transact @conn [[:db.fn/retractEntity id]])
    id))
