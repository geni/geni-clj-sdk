(ns geni.core-test
  (:use clojure.test)
  (:require [geni.core :as geni]
            [clojure.string :as string]))

(def token (string/trim-newline (slurp "token")))

(deftest read-test
  (is (= {:name "Amos Elliston", :id "profile-101"}
         (geni/read "/profile-101" {:token token, :fields ["name" "id"]}))))

(deftest write-test
  (let [document (geni/write "/document/add" {:token token, :title "foo", :text "foo"})]
    (is (= "foo" (:title document)))
    (is (= {:result "Deleted"}
           (geni/write (str "/" (:id document) "/delete") {:token token})))))
