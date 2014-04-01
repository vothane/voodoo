(ns voodoo.core
  (:require [clj-http.client :as client]
            [voodoo.ooyala :as oo]
            [clojure.string :as s]))

(def ^:dynamic *rest-api* "api.ooyala.com/v2")
(def ^:dynamic *api-key* "JkN2w61tDmKgPl4y395Rp1vAdlcq.IqBgb")
(def ^:dynamic *secret* "nU2WjeYoEY0MJKtK1DRpp1c6hNRoHgwpNG76dJkX")

(defn- get-request-uri
  [uri arg-map]
  (let [body  (:body arg-map)
        query (client/generate-query-string arg-map)]
   (str uri "?" query)))

(def http-request-map
  {"GET"  (fn [uri] (client/get (str "http://" uri)))
   "POST" (fn [uri body] (client/post uri body))})

(defn http-request
  [verb uri parameters]  
  (let [sorted-params (into (sorted-map) parameters)  
        get-request   (http-request-map verb)        
        request-uri   (get-request-uri uri sorted-params)] 
    (try        
      (get-request request-uri)
      (catch Exception e (str "caught exception: " (.getMessage e))))))

(defmacro def-ooyala-method
  [fn-name verb resource-path]
  `(defn ~fn-name
     [params#]
     (let [http-verb#  (s/upper-case (name ~verb))
           api-key#    {:api_key *api-key*}
           expiration# {:expires (oo/expires)}
           query#      (get-in params# [:params :query])
           body#       (get-in params# [:params :body])
           signature#  {:signature (oo/request-signature *secret* http-verb# ~resource-path query# body#)}
           params-map# (merge api-key# expiration# signature# query#)
           uri#        (str *rest-api* "/" ~resource-path)]
       (http-request http-verb# uri# params-map#))))

(defmacro def-ooyala-restful-method
  [verb resource-path & options]
  (let [dashed-name (clojure.string/replace resource-path #"[^a-zA-Z]+" "-")
        clean-name  (clojure.string/replace dashed-name #"-$" "")
        fn-name     (symbol clean-name)]
    `(def-ooyala-method ~fn-name ~verb ~resource-path ~@options)))