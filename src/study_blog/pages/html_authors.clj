(ns study-blog.pages.html-authors
  (:require [study-blog.pages.html-base-page :as page]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn- register-user-form [route-link]
  (form/form-to
   [:post route-link]
   [:div.mb-3
    (form/label "author-name" "Author Name")
    (form/text-field {:class "form-control"} "author-name")]
   [:div.mb-3
    (form/label "author-email" "Author Email")
    (form/text-field {:class "form-control"} "author-email")]
   [:div.mb-3
    (form/label "author-username" "Author Username")
    (form/text-field {:class "form-control"} "author-username")]
   [:div.mb-3
    (form/label "author-password" "Author password")
    (form/password-field {:class "form-control"} "author-password")]
   (anti-forgery-field)
   (form/submit-button {:class "btn btn-primary"} "Save")))

(defn register-user
  "Render the register page of an author."
  [request a]
  (if-not a
    (page/error-page)
    (let [admin? (:admin? a)
          route-link "/author"]
      (if-not admin?
        (page/error-page)
        (page/base-page
         request
         "Register author"
         (register-user-form route-link))))))

(defn- render-author-row
  ""
  [author]
  [:tr
   [:td (:name author)]
   [:td (:email author)]])

(defn- render-all-authors-table
  ""
  [authors]
  [:table.table.table-bordered.table-hover
   [:thead
    [:tr
     [:th "Name"]
     [:th "E-mail"]
     [:th "Actions"]]]
   [:tbody.table-group-divider
    (map render-author-row authors)]])

(defn list-authors
  ""
  [request a]
  (if-not a
    (page/error-page)
    (let [admin? (:admin? a)]
      (page/base-page
       request
       "All authors"
       (render-all-authors-table a)))))
