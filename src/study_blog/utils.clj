(ns study-blog.utils)

(defn require-env! [var-name]
  (or (System/getenv var-name)
      (throw (ex-info (str "Required environment variable missing: " var-name)
                      {:var var-name}))))
