(ns study-blog.schema)

(def authors-entity [{:db/ident :author/name
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/doc "The name of the author"}
                     {:db/ident :author/email
                      :db/valueType :db.type/string
                      :db/cardinality :db.cardinality/one
                      :db/unique :db.unique/identity
                      :db/doc "The e-mail of the author and his natural key"}
                     {:db/ident :author/admin?
                      :db/valueType :db.type/boolean
                      :db/cardinality :db.cardinality/one
                      :db/doc "Indicates if an author has admin powers or not. The first author registered must be an admin and only an admin can grant others the powers of admin"}])

(def articles-entity [{:db/ident :article/title
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "The title of the article"}
                      {:db/ident :article/body
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "The body of the article"}
                      {:db/ident :article/created-at
                       :db/valueType :db.type/string
                       :db/cardinality :db.cardinality/one
                       :db/doc "When the article was created"}
                      {:db/ident :article/author
                       :db/valueType :db.type/ref
                       :db/cardinality :db.cardinality/one
                       :db/doc "The author of the post. Refers to authors entity"}])

(def credentials-entity [{:db/ident :credential/username
                          :db/valueType :db.type/string
                          :db/cardinality :db.cardinality/one
                          :db/unique :db.unique/identity
                          :db/doc "The username of an author and a natural key"}
                         {:db/ident :credential/author
                          :db/valueType :db.type/ref
                          :db/cardinality :db.cardinality/one
                          :db/doc "The author of this credential. Refers to authors entity"}])

(def passwords-entity [{:db/ident :password/hash
                        :db/valueType :db.type/string
                        :db/cardinality :db.cardinality/one
                        :db/doc "The password hash of an author"}
                       {:db/ident :password/credential
                        :db/valueType :db.type/ref
                        :db/cardinality :db.cardinality/one
                        :db/unique :db.unique/identity
                        :db/doc "The credential of this password. Refers to credentials entity. Can only be one password per credential"}])

(def blog-schema (into [] (concat authors-entity
                                  articles-entity
                                  credentials-entity
                                  passwords-entity)))
