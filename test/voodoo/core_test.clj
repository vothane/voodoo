(ns voodoo.core-test
  (:require [clojure.test :refer :all]
            [voodoo.core :refer :all]
            [voodoo.query :refer :all]
            [voodoo.ooyala :refer :all]
            [clj-http.client :as client]))

(def find-video (def-ooyala-method :get "assets"))

(defn query [max desc]
  (WHERE (AND (> duration ~max) (= description ~desc))))

(deftest query-test

  (testing "search vid with duration > 10 secs and has 'Iron Man, Thor, Captain America, and the Hulk' in description"
    (let [query-string (get-ooyala-query (query 10 "Iron Man, Thor, Captain America, and the Hulk"))
          response     (find-video {:params {:query {:where query-string}}})
          result       (get-response-data response)]
      (is (= (:status response) 200))   
      (is (= (get result "name") "Avengers"))
      (is (= (get result "duration") 143477))
      (is (= (get result "description") (str "Nick Fury, the director of S.H.I.E.L.D., assembles a group of " 
                                             "superheroes that includes Iron Man, Thor, Captain America, and "
                                             "the Hulk to fight a new enemy that is threatening the safety of "
                                             "the world.")))
      (is (= (get result "embed_code") "o1NmdxMzrrWwbOVk_wIqhw-AmhlOMO49")))))


(def get-labels (def-ooyala-method :get "labels"))

(deftest get-labels-test

  (testing "get all labels"
    (let [response (get-labels)
          result   (get-response-data response)]
      (is (= (:status response) 200))
      (is (= (get result "full_name") "/Movie Trailer"))
      (is (= (get result "id") "b5e9bf9a0e5a4b31991cfdf9218c4342")))))


(def get-players (def-ooyala-method :get "players"))

(deftest players-test

  (testing "get meta-data for all video players"
    (let [response (get-players)
          result   (get-response-data response)]
      (is (= (:status response) 200))
      (is (= (get result "name") "test player"))
      (is (= (get result "playback") {"buffer_on_pause" false})))))


(def get-player (def-ooyala-method :get "players/447b53e1c17c447f8dbbd912b1fbc522"))

(deftest find-player-by-id-test

  (testing "get meta-data for particular player with id of 447b53e1c17c447f8dbbd912b1fbc522"
    (let [response (get-player)
          result   (get-response-data response)]
      (is (= (:status response) 200))
      (is (= (get result "name") "test player"))
      (is (= (get result "playback") {"buffer_on_pause" false})))))
