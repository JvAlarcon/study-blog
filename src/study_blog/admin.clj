(ns study-blog.admin
  (:require [study-blog.utils :as u]))

(def admin-login (u/require-env! "ADMIN_LOGIN"))
(def admin-password (u/require-env! "ADMIN_PASSWORD"))

(defn validate-login [login pwd]
  (and (= login admin-login)
       (= pwd admin-password)))
