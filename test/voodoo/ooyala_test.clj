(ns voodoo.ooyala-test
  (:require [clojure.test  :refer :all]
            [voodoo.ooyala :refer :all]))

(def ^{:dynamic true} *secret-key* "329b5b204d0f11e0a2d060334bfffe90ab18xqh5")

(deftest request-test-one
  (let [http-method         "post"
        request-path        "players/HbxJKM"
        query-string-params {"api_key" "7ab06" "expires" 1299991855}
        request-body        "test"]
    (is (= "fJrWCcIqeRBZUqa61OV%2B6XOWfpkab6RdW5hJZmZh1CI" 
           (request-signature *secret-key* http-method request-path query-string-params request-body)))))