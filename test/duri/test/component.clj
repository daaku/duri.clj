(ns duri.test.component
  "Test duri.component functionality."
  {:author "Naitik Shah"}
  (:use
    [duri.component :only [encode decode]]
    [clojure.test :only [deftest testing is]]))

(deftest encode-test
  (is (= "foo" (encode "foo")))
  (is (= "foo+bar" (encode "foo bar"))))

(deftest decode-test
  (is (= "foo" (decode "foo")))
  (is (= "foo bar" (decode "foo+bar"))))
