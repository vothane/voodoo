(ns voodoo.ooyala
  (:import [java.security MessageDigest]
           [org.apache.commons.codec.binary Base64]))

(def ^{:dynamic true} *hash* "SHA-256")


(defn request-signature [secret http-method request-path query-string-params request-body]
  (let [seed-string      (str secret http-method "/v2/" request-path)
        sorted-params    (sort query-string-params)
        signature-string (str (reduce (fn [seed+= k=v] (str seed+= k=v)) seed-string
                                (map (fn [param] (str (first param) "=" (second param))) sorted-params))
                           request-body)
        hash-signature   (Base64/encodeBase64String (.digest (MessageDigest/getInstance *hash*) (.getBytes signature-string)))]
    (subs hash-signature 0 43)))
