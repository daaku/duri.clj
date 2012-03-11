(ns duri.qs
  "Query String parsing and encoding functions."
  {:author "Naitik Shah"}
  (:require [duri.component :as component])
  (:use [clojure.string :only [split blank?]]))

(defn- as-str [x] (if (instance? clojure.lang.Named x) (name x) (str x)))

(defn encode
  "Encoding a map to a query string, using either a specified encoding
  or UTF-8 by default."
  [params & [encoding]]
  (if (string? params)
    params
    (->> (for [[k v] params]
           (str (component/encode (as-str k) encoding)
                "="
                (component/encode (str v) encoding)))
         (interpose "&")
         (apply str))))

(defn decode
  "Encoding a map to a query string, using either a specified encoding
  or UTF-8 by default."
  [qs & [encoding]]
  (cond
   (map? qs) qs
   (blank? qs) {}
   :else (->> (split qs #"&")
              (map #(split % #"="))
              (map (fn [[k v]] [(keyword (component/decode k))
                                (component/decode v)]))
              (into {}))))
