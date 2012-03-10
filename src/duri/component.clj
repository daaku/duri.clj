(ns duri.component
  "URI component encoding"
  {:author "Naitik Shah"}
  (:import (java.net URLEncoder URLDecoder)))

(defn encode
  "Returns the form-url-encoded version of the given string, using either a
  specified encoding or UTF-8 by default."
  [unencoded & [encoding]]
  (URLEncoder/encode unencoded (or encoding "UTF-8")))

(defn decode
  "Returns the form-url-decoded version of the given string, using either a
  specified encoding or UTF-8 by default."
  [encoded & [encoding]]
  (try
    (URLDecoder/decode encoded (or encoding "UTF-8"))
    (catch Exception e nil)))
