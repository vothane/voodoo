(ns voodoo.core-test
  (:require [clojure.test :refer :all]
            [voodoo.core :refer :all]
            [voodoo.query :refer :all]
            [clj-http.client :as client]))

(deftest http-request-test
  (let [verb   "get"
        uri    "api.ooyala.com/v2/assets"
        params {:params {:query {:where "labels INCLUDES 'Sports' AND labels INCLUDES 'Baseball'"}}}
        actual "api.ooyala.com/v2/assets?where=labels INCLUDES 'Sports' AND labels INCLUDES 'Baseball'"]
    (is (= (http-request verb uri params) actual))))

(def-ooyala-restful-method :get "assets")

(defn query [max desc]
  (WHERE (AND (< duration ~max) (= description ~desc))))

(deftest macro-test
  (let [query-string (get-ooyala-query (query 10 "Iron Man, Thor, Captain America, and the Hulk"))
        result (assets {:params {:query {:where query-string}}})
        actual "api.ooyala.com/v2/assets?where=duration<600 AND description='Iron Man, Thor, Captain America, and the Hulk'"]
    (is (= result actual))))  
