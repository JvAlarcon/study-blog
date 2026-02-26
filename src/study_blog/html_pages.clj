(ns study-blog.html-pages
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [markdown.core :as md]))

(defn base-page [& body]
  (html5
   [:head
    [:title "Study blog :)"]
    [:link {:rel "stylesheet"
            :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
            :integrity "sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
            :crossorigin "anonymous"}]]
   [:body
    [:div.container
     [:nav.navbar.navbar-expand-lg.bg-body-tertiary
      [:div.container-fluid
       [:a.navbar-brand {:href "/"} "Study Blog"]
       [:div.collapse.navbar-collapse
        [:ul.navbar-nav
         [:li.nav-item
          [:a.nav-link {:href "/articles/new"} "New article!"]]
         [:li.nav-item
          [:a.nav-link {:href "/admin/login"} "Login"]]
         ; TODO Add logout and the control if will show login or logout
         ]]]]
     body]]))

(def preview-len 270)

(defn- cut-body [body]
  (if (> (.length body) preview-len)
    (subs body 0 preview-len)
    body))

(defn index [articles]
  (base-page
   (for [a articles]
     [:div
      [:h2 [:a {:href (str "/articles/" (:id a))} (:title a)]]
      [:p (-> a
              :body
              cut-body
              md/md-to-html-string)]])))

(defn article [a]
  (println a)
  (base-page
   (form/form-to
    [:delete (str "/articles/" (:id a))]
    (anti-forgery-field)
    [:a.btn.btn-primary {:href (str "/articles/" (:id a) "/edit")} "edit"]
    (form/submit-button {:class "btn btn-danger"} "delete"))
   [:small (:created-at a)]
   [:h1 (:title a)]
   [:p (-> a
           :body
           md/md-to-html-string)]))

(defn edit-article [a]
  (base-page
   (form/form-to
    [:post (if a
             (str "/articles/" (:id a))
             "/articles")]
    [:div.mb-3
     (form/label "title" "Title")
     (form/text-field {:class "form-control"} "title" (:title a))]
    [:div.mb-3
     (form/label "body" "Content")
     (form/text-field {:class "form-control"} "body" (:body a))]
    (anti-forgery-field)
    (form/submit-button {:class "btn btn-primary"} "Save"))))

(defn admin-login [& [msg]]
  (base-page
   (when msg
     [:div.alert.alert-danger msg])
   (form/form-to
    [:post "/admin/login"]
    [:div.mb-3
     (form/label "login" "Username")
     (form/text-field {:class "form-control"} "login")]
    [:div.mb-3
     (form/label "password" "Password")
     (form/text-field {:class "form-control"} "password")]
    (anti-forgery-field)
    (form/submit-button {:class "btn btn-primary"} "Login"))))
