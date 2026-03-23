(ns study-blog.pages.html-articles
  (:require [study-blog.pages.html-base-page :as page]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [markdown.core :as md]))

(def preview-len 270)

(defn- cut-body [body]
  (if (> (.lenght body) preview-len)
    (subs body 0 preview-len)
    body))

(defn- article-preview
  "Helper function to create a link and show a preview of the article"
  [a]
  [:div
   [:h2 [:a {:href (str "/articles" (:id a))} (:title a)]]
   [:p (-> a
           :body
           cut-body
           md/md-to-html-string)]])

(defn index
  "Render index page.
  Recives the ring request and a list of articles and will return links to the articles and it's preview"
  [request articles]
  (page/base-page
   request
   "Index"
   (map article-preview articles)))


(defn article
  "Render the article page.
  Receives the ring request and the article and return the information of the article"
  [request a]
  (page/base-page
   request
   (str (:title a))
   (form/form-to
    [:delete (str "/articles/" (:id a))]
    (anti-forgery-field)
    [:a.btn.btn-primary {:href (str "/articles/" (:id a) "/edit")} "edit"]
    (form/submit-button {:class "btn btn-danger"} "delete"))
   [:small (:created-at a)]
   [:small (str "Author: " :author)]
   [:h1 (:title a)]
   [:p (-> a
           :body
           md/md-to-html-string)]))

(defn edit-article
  "Render the creation or edit page."
  [request a]
  (page/base-page
   request
   (if a
     (str "Editing - " (:title a))
     "Article creation")
   (form/form-to
    [:post (if a
             (str "/articles/" (:id a))
             "/articles")]
    [:div.mb-3
     (form/label "title" "Title")
     (form/text-field {:class "form-control"} "title" (:title a))]
    [:div.mb-3
     (form/label "body" "Content")
     (form/text-area {:rows "50" :cols "100"} "body" (:body a))]
    (anti-forgery-field)
    (form/submit-button {:class "btn btn-primary"} "Save"))))
