(ns study-blog.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as res]
            [ring.middleware.session :as session]
            [study-blog.database :as db]
            [study-blog.html-pages :as page]
            [study-blog.admin :as adm]))

(defroutes app-routes
  (GET "/" []
       (page/index (db/list-articles)))
  (GET "/articles/:art-id" [art-id]
       (page/article (db/get-article art-id)))
  (POST "/admin/login" [login password]
       (if (adm/validate-login login password)
         (-> (res/redirect "/")
             (assoc-in [:session :admin] true))
         (page/admin-login "Invalid username or password")))
  (GET "admin/logout" []
       (-> (res/redirect "/")
           (assoc-in [:session :admin] false)))
  (rout/not-found "Not found"))

(defroutes adm-routes
  (GET "/articles/new" []
       (page/edit-article nil))
  (POST "/articles" [title body]
        (do (db/create-article title body)
            (res/redirect "/")))
  (GET "/articles/:art-id/edit" [art-id]
       (page/edit-article (db/get-article art-id)))
  (POST "/articles/:art-id" [art-id title body]
        (do (db/update-article art-id title body)
            (res/redirect (str "articles/" art-id))))
  (DELETE "/articles/:arti-id" [art-id]
          (do (db/delete-article art-id)
              (res/redirect "/"))))

(defn wrap-adm-only [handler]
  (fn [request]
    (if (-> request :session :admin)
      (handler request)
      (res/redirect "/admin/login"))))

(def app
  (db/setup!)
  (-> (routes (wrap-routes adm-routes wrap-adm-only)
              app-routes)
      (wrap-defaults site-defaults)
      session/wrap-session))
