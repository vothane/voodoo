(ns voodoo.query-test
  (:require [clojure.test :refer :all]
            [voodoo.query :refer :all]))

(defn query [max desc]
  (WHERE (AND (< duration ~max) (= description ~desc))))

(deftest query-test
  (let [result (get-ooyala-query (query 600 "cat funny"))
        actual "duration<600 AND description='cat funny'"]
    (is (= result actual))))
