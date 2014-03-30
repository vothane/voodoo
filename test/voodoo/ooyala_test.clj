(ns voodoo.ooyala-test
  (:require [clojure.test  :refer :all]
            [voodoo.ooyala :refer :all]))

(def ^{:dynamic true} *secret-key* "nU2WjeYoEY0MJKtK1DRpp1c6hNRoHgwpNG76dJkX")

(deftest request-test-one
  (let [http-method         "GET"
        request-path        "/v2/assets"
        query-string-params {"api_key" "JkN2w61tDmKgPl4y395Rp1vAdlcq.IqBgb" "expires" 1577898300}
        request-body        nil]
    (is (= "ClzQGSks95XkKXcGig5StzjtjGLlzAuD3ZtIT0TQReA" 
           (request-signature *secret-key* http-method request-path query-string-params request-body)))))