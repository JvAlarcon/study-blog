(defproject study-blog "0.1.0-SNAPSHOT"
  :description "A blog written in Clojure using Datomic"
  :min-lein-version "2.0.0"
  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"}}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [compojure "1.7.1"]
                 [ring/ring-defaults "0.5.0"]
                 [hiccup "2.0.0"]
                 [markdown-clj "1.12.4"]
                 [org.postgresql/postgresql "42.7.1"]
                 [com.datomic/peer "1.0.7491"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler study-blog.handler/app
         :open-browser? false}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}})
