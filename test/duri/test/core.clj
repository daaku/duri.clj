(ns duri.test.core
  "Test duri.core functionality."
  {:author "Naitik Shah"}
  (:use
    [duri.core :only [parse build]]
    [clojure.test :only [deftest testing is]]))

(deftest parse-test
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
  (is (= "https://foo.com/"
         (build {:path "/"
                 :host "foo.com"
                 :scheme "https"})))
  (is (= "https://foo.com/"
         (build {:host "foo.com"
                 :scheme "https"})))
  (is (= "https://foo.com/foo?bar=1#pound"
         (build {:path "/foo"
                 :host "foo.com"
                 :scheme "https"
                 :query {:bar "1"}
                 :fragment "pound"}))))
