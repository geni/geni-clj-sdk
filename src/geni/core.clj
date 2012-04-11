(ns geni.core
  (:refer-clojure :exclude [read parse])
  (:require [clj-http.client :as http]
            [clojure.string :as string]
            [cheshire.core :as json]))

(def ^:dynamic *base* "https://www.geni.com/api")

(defn ^:private parse [res]
  (json/parse-string (:body res) true))

(defn ^:private join-seqs
  "Join any values in the map that are seqs with commas."
  [m]
  (into {}
        (for [[k v] m]
          [k (if (coll? v)
               (string/join "," v)
               v)])))

(defn api
  "Calls the Geni API. Expects at least a a path, which is
  an API endpoint. For example, /profiles-101. If the API call
  requires parameters, they are passed as a map as the second
  argument. The final param, method, is either :get or :post.
  It defaults to :get, but you'll need to set :post for write
  methods.

  See the `read` and `write` functions for convenience."
  ([path] (api path {} :get))
  ([path params] (api path params :get))
  ([path params method]
   (parse
     (http/request
       (merge
         {:method method
          :url (str *base* path)
          :insecure? (:insecure? params)}
         (let [params (-> params
                          (assoc :access_token (:token params))
                          (dissoc :token :insecure?))]
           (if (= :post method)
             {:body (json/generate-string (dissoc params :access_token))
              :query-params {:access_token (:access_token params)}
              :headers {"Content-Type" "application/json"}}
             {:query-params (join-seqs params)})))))))

(defn read
  "Read from the Geni API with a GET request. See `api`."
  ([path] (read path {}))
  ([path params]
   (api path params)))

(defn write
  "Write to the Geni API with a POST request. See `api`."
  ([path] (write path {}))
  ([path params]
   (api path params :post)))
