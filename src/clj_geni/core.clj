(ns clj-geni.core
  (:require [clj-http.client :as http]
            [clojure.string :as string]
            [cheshire.core :as json]))

(def base "https://www.geni.com/api/")

(defn parse [res]
  (json/parse-string (:body res) true))

(defn json-date [options]
  (if-let [date (:date options)]
    (assoc options :date (string/join "-" ((juxt :day :month :year) date)))
    options))

(defn join-key [options key]
  (if-let [item (options key)]
    (assoc options key (string/join "," item))
    options))

(defn req [method url required optional]
  (parse
    (http/request
      (merge
        {:throw-exceptions false
         :method method
         :url (doto (str base (apply format url required)) prn)}
        {(if (#{:put :post :delete} method)
           :form-params
           :query-params) (assoc optional :access_token (:token optional))}))))

(defn document [document & [options]]
  (req :get (str "document-%s") [document] options))

