(ns voodoo.core)

(def ^:dynamic *rest-api* "api.ooyala.com/v2")

(defn- get-request-args
  [verb uri arg-map]
  (let [body      (:body arg-map)
        query     (reduce (fn [seed+= k=v] (str seed+= k=v)) "?"
                     (map (fn [param] (str (name (first param)) "=" (second param))) (:query (:params arg-map))))
        final-uri (str uri query)]
   (final-uri)))