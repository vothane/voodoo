(ns voodoo.core)

(def ^:dynamic *rest-api* "api.ooyala.com/v2")

(defn- get-request-args
  [verb uri arg-map]
  (let [body      (:body arg-map)
        query     (reduce (fn [seed+= k=v] (str seed+= k=v)) "?"
                     (map (fn [param] (str (name (first param)) "=" (second param))) (:query (:params arg-map))))]
   (str uri query)))

(defn http-request
  [verb uri parameters]
  (let [request-args (get-request-args verb uri parameters)]
    (str request-args)))

(defmacro def-ooyala-method
  [fn-name verb resource-path & options]
  (let [opts (apply sorted-map options)]
    `(defn ~fn-name
       [params#]
       (let [params-map# (merge ~opts params#)
             http-verb#  (name ~verb)
             uri#        (str *rest-api* "/" ~resource-path)]
         (http-request http-verb# uri# params-map#)))))

(defmacro def-ooyala-restful-method
  [verb resource-path & options]
  (let [dashed-name (clojure.string/replace resource-path #"[^a-zA-Z]+" "-")
        clean-name  (clojure.string/replace dashed-name #"-$" "")
        fn-name     (symbol clean-name)]
    `(def-ooyala-method ~fn-name ~verb ~resource-path ~@options)))
