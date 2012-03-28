(ns clj-geni.profiles
  (:require [clj-geni.core :as core]
            [clojure.string :as string]))

(defn search
  "http://www.geni.com/platform/developer/help/api?path=profile%2Fsearch&version=1
  `names` is a sequence of strings that will be joined together
  with commas"
  [names & [options]]
  (core/req :get "profile/search" [] (assoc options :names (string/join "," names))))
