(ns study-blog.database.db-authors
  (:require [datomic.api :as d]
            [study-blog.database.core :as db-core]))

; Add credentials entity and password entity to create author
(defn create-author
  "Store an author inside datomic.
  If a boolean value for admin powers is not provided, the default will be false.
  It will return the id stored in datomic."
  ([name email username password]
   (create-author name email username password false))
  ([name email username password admin?]
   (let [author-temp-id "new-author"
         credential-temp-id "new-credential"
         password-temp-id "new-password"
         result @(d/transact (db-core/get-conn) [{:db/id author-temp-id
                                                  :author/name name
                                                  :author/email email
                                                  :author/admin? admin?}
                                                 {:db/id credential-temp-id
                                                  :credentail/username username
                                                  :credentail/author author-temp-id}
                                                 {:db/id password-temp-id
                                                  :password/hash password
                                                  :password/credential credential-temp-id}])]
     (get-in result [:tempids author-temp-id]))))

(def query-all-authors
  '[:find ?e ?name ?email
    :keys id name email
    :where [?e :author/name ?name]
           [?e :author/email ?email]])

(defn list-authors
  "List all authors stored in datomic. It will return a list of maps of all the authors"
  []
  (let [db (db-core/get-db)]
    (d/q query-all-authors db)))

(defn get-author
  "Given a id in string format, return a map with the data of the author"
  [str-id]
  (let [db (db-core/get-db)
        id (parse-long str-id)]
    (->
     (d/pull db '[:db/id :author/name :author/email :author/admin?] id)
     db-core/remove-ns-from-keys)))

(defn update-author
  "Updates the data of an authors stored in datomic.
  Email can't be updated because it's a key.
  You can update or the admin powers alone or update the name and the admin powrs.
  It will return the id of the author"
  ([str-id admin?]
   (let [id (parse-long str-id)]
     @(d/transact (db-core/get-conn) [{:db/id id
                                      :author/admin? admin?}])))
  ([str-id name admin?]
   (let [id (parse-long str-id)]
     @(d/transact (db-core/get-conn) [{:db/id id
                                       :author/name name
                                       :author/admin? admin?}])
     id)))

(defn delete-author
  "Given an author id, delete this author from datomic.
  It will return the previous id of the deleted author"
  [str-id]
  (let [id (parse-long str-id)]
    @(d/transact (db-core/get-conn) [[:db.fn/retractEntity id]])
    id))

(def users-entity [{:db/ident :user/name
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The name of the user"}])


(def credentials-entity [{:db/ident :credential/username
                          :db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/unique :db.unique/identity
                          :db/doc "The username of an user and a natural key"}
                         {:db/ident :credential/user
                          :db/valueType :db.type/ref
                          :db/cardinality :db.cardinality/one
                          :db/doc "The users of this credential. Refers to user's entity"}])

(def passwords-entity [{:db/ident :password/hash
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "The password hash of an user"}
                      {:db/ident :password/credential
                       :db/valueType :db.type/ref
                       :db/cardinality :db.cardinality/one
                       :db/unique :db.unique/identity
                       :db/doc "The credential of this password. Refers to credentials entity. Can only be one password per credential"}])
