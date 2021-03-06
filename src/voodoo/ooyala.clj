(ns voodoo.ooyala
  (:require [clojure.data.codec.base64 :as Base64]
            [clojure.string :as s]
            [cheshire.core :refer :all])
  (:import [java.security MessageDigest]))

(def ^{:dynamic true} *hash* "SHA-256")
(def ^{:dynamic true} *round-up-time* 300)

(defn- clean-signature
  [sign]
  (let [sign-trimmed (s/trim sign)
        reverse-sign (s/reverse sign-trimmed)]
    (if (= (first reverse-sign) \=)
      (s/reverse (s/replace-first reverse-sign "=" ""))
      sign-trimmed)))

(defn request-signature 
  [secret http-method request-path query-string-params request-body]
  (let [seed-string      (str secret (s/upper-case http-method) request-path)
        sorted-params    (sort query-string-params)
        signature-string (str (reduce (fn [seed+= k=v] (str seed+= k=v)) seed-string
                                (map (fn [param] (str (name (first param)) "=" (second param))) sorted-params))
                           (when-not (nil? request-body) (generate-string request-body)))
        hash-signature   (Base64/encode (.digest (MessageDigest/getInstance *hash*) (.getBytes signature-string)))]       
    (clean-signature (subs (String. hash-signature) 0 43))))

(defn expires
  []
  (let [now+expiration-window (+ (/ (System/currentTimeMillis) 1000) 100)
        round-up              (- *round-up-time* (mod now+expiration-window *round-up-time*))]
    (+ now+expiration-window round-up)))

(defn get-response-data
  [response]
  (let [parsed-resp (parse-string (:body response))]
    (if (contains? parsed-resp "items")
      (first (get parsed-resp "items"))
      parsed-resp)))
;(tree-seq map? (fn [sub-map] (flatten (seq sub-map))) (json/read-str (:body response)))