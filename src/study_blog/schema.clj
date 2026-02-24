(ns study-blog.schema)

(def blog-schema [{:db/ident :blog/title
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The title of the article"}
                  {:db/ident :blog/body
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "The body of the article"}
                  {:db/ident :blog/created-at
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "When the article was created"}])
