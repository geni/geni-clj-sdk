(ns geni.core
  (:refer-clojure :exclude [read parse])
  (:require [clj-http.client :as http]
            [clojure.string :as string]
            [cheshire.core :as json]))

(def ^:dynamic *base* "https://www.geni.com/api")
(def ^:dynamic *insecure* false)
(def ^:dynamic *access-token* nil)
(def ^:dynamic *basic-auth* nil)

(defn ^:private parse [res]
  (json/parse-string (:body res)))

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
  an API endpoint. For example, /profile-101. If the API call
  requires parameters, they are passed as a map as the second
  argument. The final param, method, is either :get or :post.
  It defaults to :get, but you'll need to set :post for write
  methods.

  See the `read` and `write` functions for convenience."
  ([method path params] (api method path {} params))
  ([method path data params]
     (parse
      (http/request
       (merge
        {:method method
         :basic-auth *basic-auth*
         :throw-exceptions false
         :url (str *base* path)
         :insecure? *insecure*}
        (let [params (merge {:access_token *access-token*}
                            params)]
          (if (= :post method)
            {:body (json/generate-string data)
             :query-params params
             :headers {"Content-Type" "application/json"}}
            {:query-params (join-seqs params)})))))))

(defn read
  "Read from the Geni API with a GET request. See `api`."
  ([path] (read path {}))
  ([path params]
     (api :get path {} params)))

(defn write
  "Write to the Geni API with a POST request. See `api`."
  ([path data] (write path data {}))
  ([path data params]
     (api :post path data params)))
