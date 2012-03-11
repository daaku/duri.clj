(ns duri.test.qs
  "Test duri.qs functionality."
  {:author "Naitik Shah"}
  (:require [duri.qs :as qs])
  (:use [duri.qs :only [encode decode php-flatten underscore-keys]]
        [clojure.test :only [deftest testing is]]))

(deftest encode-test
  (is (= "foo=1" (encode {:foo 1})))
  (is (= "foo+bar=baz+one" (encode {"foo bar" "baz one"})))
  (is (= "blah" (encode "blah"))))

(deftest decode-test
  (is (= {:foo "1"} (decode "foo=1")))
  (is (= {:foo "baz one"} (decode "foo=baz+one")))
  (is (= {} (decode {})))
  (is (= {:a 1} (decode {:a 1})))
  (is (= {} (decode "")))
  (is (= {} (decode false)))
  (is (= {} (decode nil))))

(deftest merge-test
  (is (= {:a 1} (qs/merge nil {:a 1})))
  (is (= {:a "1"} (qs/merge nil "a=1")))
  (is (= {:a 1 :b "2"} (qs/merge {:a 1} "b=2")))
  (is (= {:a "1" :b "2"} (qs/merge "a=1" "b=2"))))

(deftest php-flatten-test
  (is (= {"a[b]" 1} (php-flatten {:a {:b 1}}))))

(deftest underscore-keys-test
  (is (= {"a_b" 1} (underscore-keys {:a-b 1}))))
