(ns study-blog.database.core
  (:require [datomic.api :as d]
            [study-blog.schema :as schema]
            [study-blog.utils :as u]))

(def db-connection-uri (u/require-env! "DB_URI"))

; Connection in a singleton pattern to have one and only one connection
; It will be initialized by `init!` function, at startup.
; Use get-conn to access it safely
(defonce conn (atom nil))

(defn init!
  "Initializes the connection when called. It will assign a connection to `conn`."
  []
  (d/create-database db-connection-uri)
  (let [c (d/connect db-connection-uri)]
    (d/transact c schema/blog-schema)
    (reset! conn c)))

(defn get-conn
  "Retrives the connection definied previsouly. If not initialized by `init!`, it will throw an exception"
  []
  (or @conn (throw (ex-info "DB not initiliazed. Call init! first." {}))))

(defn get-db
  "Return a current database snapshot."
  []
  (d/db (get-conn)))

(defn remove-ns-from-keys
  "Remove all namespaces from the keys of a map.
  Example: If the map looks like `{:ns/key}` then it will remove `ns/` from the key, thus resulting in `{:key}`"
  [m]
  (update-keys m (comp keyword name)))
