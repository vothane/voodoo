(ns voodoo.ooyala
  (:require [clojure.data.codec.base64 :as Base64])
  (:import [java.security MessageDigest]))

(def ^{:dynamic true} *hash* "SHA-256")

(defn request-signature [secret http-method request-path query-string-params request-body]
  (let [seed-string      (str secret http-method "/v2/" request-path)
        sorted-params    (sort query-string-params)
        signature-string (str (reduce (fn [seed+= k=v] (str seed+= k=v)) seed-string
                                (map (fn [param] (str (first param) "=" (second param))) sorted-params))
                           request-body)
        hash-signature   (Base64/encode (.digest (MessageDigest/getInstance *hash*) (.getBytes signature-string)))]
    (subs (String. hash-signature) 0 43)))
