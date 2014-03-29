(ns voodoo.query-test
  (:require [clojure.test :refer :all]
            [voodoo.query :refer :all]))

(defn query [max desc]
  (WHERE (AND (< duration ~max) (= description ~desc))))

(deftest query-test
  (let [result (get-ooyala-query (query 600 "cat funny"))
        _      (println result)
        actual "where=duration < 600 AND description = cat funny"]
    (is (= result actual))))

;(reduce (fn [a b] (str/replace-first a "?" b)) (first t) (second t))
;"where=duration < 600 AND description = cat funny"