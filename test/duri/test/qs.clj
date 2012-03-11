(ns duri.test.qs
  "Test duri.qs functionality."
  {:author "Naitik Shah"}
  (:use
    [duri.qs :only [encode decode]]
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
