(ns voodoo.core-test
  (:require [clojure.test :refer :all]
            [voodoo.core :refer :all]
            [voodoo.query :refer :all]
            [clj-http.client :as client]))

(def-ooyala-restful-method :get "assets")

(defn query [max desc]
  (WHERE (AND (< duration ~max) (= description ~desc))))

(deftest macro-test
  (let [query-string (get-ooyala-query (query 10 "Iron Man, Thor, Captain America, and the Hulk"))
        result (assets {:params {:query {:where query-string}}})
        actual "api.ooyala.com/v2/assets?where=duration<600 AND description='Iron Man, Thor, Captain America, and the Hulk'"]
    (is (= result actual))))  
