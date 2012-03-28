(ns clj-geni.users
  (:require [clj-geni.core :as core]))

(defn me
  "http://www.geni.com/platform/developer/help/api?path=user&version=1"
  [options]
  (core/req :get "user" [] options))

