(ns duri.test.core
  "Test duri.core functionality."
  {:author "Naitik Shah"}
  (:use
    [duri.core :only [parse build append-query]]
    [clojure.test :only [deftest testing is]]))

(deftest parse-test
  (is (= {} (parse {})))
  (is (= (parse "https://foo.com/")
         {:path "/"
          :host "foo.com"
          :scheme "https"}))
  (is (= (parse "https://foo.com")
         {:path "/"
          :host "foo.com"
          :scheme "https"}))
  (is (= (parse "https://foo.com/foo?bar=1#pound")
         {:path "/foo"
          :host "foo.com"
          :scheme "https"
          :query {:bar "1"}
          :fragment "pound"})))

(deftest build-test
  (is (= "a" (build "a")))
  (is (= "https://foo.com/"
         (build {:path "/"
                 :host "foo.com"
                 :scheme "https"})))
  (is (= "https://foo.com/"
         (build {:host "foo.com"
                 :scheme "https"})))
  (is (= "https://foo.com:81/"
         (build {:host "foo.com"
                 :scheme "https"
                 :port 81})))
  (is (= "https://foo.com/"
         (build {:host "foo.com"
                 :scheme "https"
                 :port nil})))
  (is (= "https://foo.com/foo?bar=1#pound"
         (build {:path "/foo"
                 :host "foo.com"
                 :scheme "https"
                 :query {:bar "1"}
                 :fragment "pound"}))))

(deftest append-query-test
  (is (= {:a "1" :b "2"} (:query (append-query "/?a=1" {:b "2"})))))
