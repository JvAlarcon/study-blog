(ns study-blog.database
  (:require [datomic.api :as d]
            [study-blog.schema :as schema]
            [study-blog.utils :as u])
  (:import [java.time LocalDateTime]
           [java.time.format DateTimeFormatter]))

(def db-connection-uri (u/require-env! "DB_URI"))
(def conn (atom nil))
(def dt-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss"))

(defn setup! []
  (d/create-database db-connection-uri)
  (let [c (d/connect db-connection-uri)]
    (d/transact c schema/blog-schema)
    (reset! conn c)))

(defn create-article [title body]
  (let [dt-now (.format (LocalDateTime/now) dt-formatter)]
    (d/transact @conn [{:blog/title title
                        :blog/body body
                        :blog/created-at dt-now}])))

(def query-all-articles
  '[:find ?e ?title ?body ?created-at
    :keys id title body created-at
    :where [?e :blog/title ?title]
           [?e :blog/body ?body]
           [?e :blog/created-at ?created-at]])

(defn list-articles []
  (let [db (d/db @conn)]
    (d/q query-all-articles db)))

(defn get-article [id]
  (let [db (d/db @conn)]
    (d/pull db '[:blog/title :blog/body :blog/created-at] id)))

(defn update-article [id title body]
  (d/transact @conn [{:blog/id id
                      :blog/title title
                      :blog/body body}]))

(defn delete-article [id]
  (d/transact @conn [[:db.fn/retractEntity id]]))
