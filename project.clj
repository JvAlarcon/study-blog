(defproject study-blog "0.1.0-SNAPSHOT"
  :description "A blog written in Clojure using Datomic"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-default "0.3.2"]
                 [hiccup "2.0.0"]
                 [markdown-clj "1.12.4"]]
  :pluggins [[lein-ring "0.12.5"]]
  :ring {:handler study-blog/app}
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}})
