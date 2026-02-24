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
         ; Add logout and the control if will show login or logout
         ]]]]
     body]]))

(def preview-len 270)

(defn- cut-body [body]
  (if (> (.length body) preview-len)
    (subs body 0 preview-len)
    body))


