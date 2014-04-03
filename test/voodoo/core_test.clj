(ns voodoo.core-test
  (:require [clojure.test :refer :all]
            [voodoo.core :refer :all]
            [voodoo.query :refer :all]
            [voodoo.ooyala :refer :all]
            [clj-http.client :as client]))

(def-ooyala-restful-method :get "assets")

(defn query [max desc]
  (WHERE (AND (> duration ~max) (= description ~desc))))

(deftest macro-test
  (let [query-string (get-ooyala-query (query 10 "Iron Man, Thor, Captain America, and the Hulk"))
        response     (assets {:params {:query {:where query-string}}})
        result       (get-response-data response)]
    (is (= (get result "name") "Avengers"))
    (is (= (get result "duration") 143477))
    (is (= (get result "description") (str "Nick Fury, the director of S.H.I.E.L.D., assembles a group of " 
                                           "superheroes that includes Iron Man, Thor, Captain America, and "
                                           "the Hulk to fight a new enemy that is threatening the safety of "
                                           "the world.")))
    (is (= (get result "embed_code") "o1NmdxMzrrWwbOVk_wIqhw-AmhlOMO49"))))  
