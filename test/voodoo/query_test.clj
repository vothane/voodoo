(ns voodoo.query-test
  (:require [clojure.test  :refer :all]
            [voodoo.query :refer :all]))

(deftest query-test
  (let [result (query 600 "cat funny")
        actual ["where=duration < ? AND description = ?" [600 "cat funny"]]]
    (is (= result actual))))

;(reduce (fn [a b] (str/replace-first a "?" b)) (first t) (second t))
;"where=duration < 600 AND description = cat funny"