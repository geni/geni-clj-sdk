(ns clj-geni.core
  (:refer-clojure :exclude [read parse])
  (:require [clj-http.client :as http]
            [clojure.string :as string]
            [cheshire.core :as json]))

(def ^:private base "https://www.geni.com/api")

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
  ([url] (api url {} :get))
  ([url params] (api url params :get))
  ([url params method]
   (parse
     (http/request
       (merge
         {:method method
          :url (str base url)}
         (let [params (-> params
                          (assoc :access_token (:token params))
                          (dissoc :token)
                          join-seqs)]
           (if (= :post method)
             {:form-params params}
             {:query-params params})))))))

(defn read
  ([url] (read url {}))
  ([url params]
   (api url params)))

(defn write
  ([url] (write url {}))
  ([url params]
   (api url params :post)))
