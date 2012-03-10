(ns duri.test.qs
  "Test duri.qs functionality."
  {:author "Naitik Shah"}
  (:use
    [duri.qs :only [encode decode]]
    [clojure.test :only [deftest testing is]]))

(deftest encode-test
  (is (= "foo=1" (encode {:foo 1})))
  (is (= "foo+bar=baz+one" (encode {"foo bar" "baz one"}))))

(deftest decode-test
  (is (= {:foo "1"} (decode "foo=1")))
  (is (= {:foo "baz one"} (decode "foo=baz+one")))
  (is (= {} (decode "")))
  (is (= {} (decode false)))
  (is (= {} (decode nil))))
