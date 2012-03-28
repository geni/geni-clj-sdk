(ns clj-geni.documents
  (:require [clj-geni.core :as core]
            [cheshire.core :as json]))

(defn read
  "http://www.geni.com/platform/developer/help/api?path=document&version=1"
  [document & [options]]
  (core/req :get (str "document-%s") [document]
            (-> options (core/join-key :fields) (core/join-key :ids))))

(defn add
  "http://www.geni.com/platform/developer/help/api?path=document%2Fadd&version=1"
  [title options]
  (core/req :post "document/add" nil
            (assoc (core/json-date options)
                   :title title)))

(defn comment
  "http://www.geni.com/platform/developer/help/api?path=document%2Fcomment&version=1"
  [id text options]
  (core/req :post "document-%s/comment" [id] (assoc options :text text)))

(defn comments
  "http://www.geni.com/platform/developer/help/api?path=document%2Fcomments&version=1"
  [id & [options]]
  (core/req :get "document-%s/comments" [id] (core/join-key options :fields)))

(defn delete
  "http://www.geni.com/platform/developer/help/api?path=document%2Fdelete&version=1"
  [id options]
  (core/req :post "document-%s/delete" [id] options))

(defn projects
  "http://www.geni.com/platform/developer/help/api?path=document%2Fprojects&version=1"
  [id & [options]]
  (core/req :get "document-%s/projects" [id] (core/join-key options :fields)))

(defn tag
  "http://www.geni.com/platform/developer/help/api?path=document%2Ftag&version=1"
  [id profile-id options]
  (core/req :post "document-%s/tag/profile-%s" [id profile-id] options))

(defn tags
  "http://www.geni.com/platform/developer/help/api?path=document%2Ftags&version=1"
  [id options]
  (core/req :get "document-%s/tags" [id] (core/join-key options :fields)))

(defn untag
  "http://www.geni.com/platform/developer/help/api?path=document%2Funtag&version=1"
  [id profile-id options]
  (core/req :post "document-%s/untag/profile-%s" [id profile-id] options))

(defn update
  "http://www.geni.com/platform/developer/help/api?path=document%2Fupdate&version=1"
  [id options]
  (core/req :post "document-%s/update" [id]
            (-> options core/json-date (core/join-key :labels))))
