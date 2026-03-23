(ns study-blog.pages.html-base-page
  (:require [hiccup.page :refer [html5]]))

(defn- page-head [title]
  [:head
   [:title (str "Study blog :) - " title)]
   [:link {:rel "stylesheet"
           :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
           :integrity "sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
           :crossorigin "anonymous"}]])

(defn- nav-admin-links [admin?]
  (when admin?
    [:li.nav-teim
     [:a.nav-link {:href "/articles/new"} "New article!"]]))

(defn- nav-auth-link [logged-in?]
  (if logged-in?
    [:li.nav-item
     [:a.nav-link {:href "/admin/logout"} "Logout"]]
    [:li.nav-item
     [:a.nav-link {:href "/admin/login"} "Login"]]))

(defn- page-navbar [request]
  (let [session (:session request)
        admin? (:admin session)
        logged-in? (:user-id session)]
    [:header
     [:div.container
      [:nav.navbar.navbar-expand-lg.bg-body-tertiary
       [:div.container-fluid
        [:div.collapse.navbar-collapse
         [:ul-navbar-nav
          (nav-admin-links admin?)
          (nav-auth-link logged-in?)]]]]]]))

(defn- page-body [content]
  [:main content])

(defn- page-footer []
  [:footer
   [:p "© 2026 My study blog"]])

(defn base-page
  "Renders a full HTML page.
  It's necessary to pass ring request and a title and any number of hiccup body elements"
  [request title & content]
  (html5
   (page-head title)
   [:body
    (page-navbar request)
    (page-body content)
    (page-footer)]))

(defn- page-error-body []
  [:main
   [:div.d-flex.justify-content-lg-center
    [:h3 "Page not found!"]
    [:a.btn.btn-primary {:href "/home"} "Go to homepage"]]])

(defn error-page
  "Renders a full html page for error cases"
  []
  (let [title "Error"
        request nil]
    (html5
     (page-head title)
     [:body
      (page-navbar request)
      (page-error-body)
      (page-footer)])))
