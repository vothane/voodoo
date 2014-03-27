(ns voodoo.core-test
  (:require [clojure.test :refer :all]
            [voodoo.core :refer :all]))

(deftest http-request-test
  (let [verb   "get"
        uri    "api.ooyala.com/v2/assets"
        params {:params {:query {:where "labels INCLUDES 'Sports' AND labels INCLUDES 'Baseball'"}}}
        actual "api.ooyala.com/v2/assets?where=labels INCLUDES 'Sports' AND labels INCLUDES 'Baseball'"]
    (is (= (http-request verb uri params) actual))))

(def-ooyala-restful-method :get "assets")

(deftest macro-test
  (let [result (assets {:params {:query {:where "labels INCLUDES 'Sports' AND labels INCLUDES 'Baseball'"}}})
        actual "api.ooyala.com/v2/assets?where=labels INCLUDES 'Sports' AND labels INCLUDES 'Baseball'"]
    (is (= result actual))))  
