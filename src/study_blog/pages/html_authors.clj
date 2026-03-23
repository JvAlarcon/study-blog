(ns study-blog.pages.html-authors
  (:require [study-blog.pages.html-base-page :as page]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [compojure.route :as route]))

(defn- register-user-form [route-link]
  (form/form-to
   [:post route-link]
   [:div.mb-3
    (form/label "author-name" "Author Name")
    (form/text-field {:class "form-control"} "author-name")]
   []
   (anti-forgery-field)
   (form/submit-button {:class "btn btn-primary"} "Save")))

(defn register-user
  ""
  [request a]
  (if-not a
    (page/error-page)
    (let [admin? (:admin? a)
          route-link "/author"]
      (if-not admin?
        (page/error-page)
        (register-user-form route-link)))))
