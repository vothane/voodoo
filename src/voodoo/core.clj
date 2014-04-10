(ns voodoo.core
  (:require [clj-http.client :as client]
            [voodoo.ooyala :as oo]
            [clojure.string :as s]
            [cheshire.core :refer :all]))

(def ^:dynamic *rest-api* "api.ooyala.com/v2")
(def ^:dynamic *api-key* "JkN2w61tDmKgPl4y395Rp1vAdlcq.IqBgb")
(def ^:dynamic *secret* "nU2WjeYoEY0MJKtK1DRpp1c6hNRoHgwpNG76dJkX")

(defn- get-request-uri
  [uri arg-map]
  (let [body  (:body arg-map)
        query (client/generate-query-string arg-map)]
   (str uri "?" query)))

(def http-request-map
  {"GET"    (fn [uri & ignore] (client/get uri))
   "POST"   (fn [uri body]     (client/post uri {:body (generate-string body)}))
   "DELETE" (fn [uri & ignore] (client/delete uri))
   "PUT"    (fn [uri body]     (client/put uri {:body (generate-string body)}))
   "PATCH"  (fn [uri body]     (client/patch uri {:body (generate-string body)}))})

(defn http-request
  [verb uri parameters body]  
  (let [sorted-params (into (sorted-map) parameters)  
        get-request   (get http-request-map verb)        
        request-uri   (get-request-uri uri sorted-params)] 
    (try                           
      (get-request request-uri body)
    (catch Exception e (str "caught exception: " (.getMessage e))))))

(defmacro def-ooyala-method
  [verb resource-path]
  `(fn [& [params#]]
     (let [http-verb#  (s/upper-case (name ~verb))
           api-key#    {:api_key *api-key*}
           expiration# {:expires (oo/expires)}
           query#      (get-in params# [:params :query])
           body#       (get-in params# [:params :body])
           params-map# (merge api-key# expiration# query#)
           signature#  {:signature (oo/request-signature *secret* http-verb# ~(str "/v2/" resource-path) params-map# body#)}
           all-params# (merge params-map# signature#)
           uri#        (str "https://" *rest-api* "/" ~resource-path)]
       (http-request http-verb# uri# all-params# body#))))
