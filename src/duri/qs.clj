(ns duri.qs
  "Query String parsing and encoding functions."
  {:author "Naitik Shah"}
  (:refer-clojure :exclude [merge])
  (:require [duri.component :as component])
  (:use [clojure.string
         :only [split replace blank?]
         :rename {replace str-replace}]))

(defn- as-str [x] (if (instance? clojure.lang.Named x) (name x) (str x)))

(defn encode
  "Encoding a map to a query string, using either a specified encoding
  or UTF-8 by default."
  [params & [encoding]]
  (cond
    (empty? params) ""
    (string? params) params
    :else (->> (for [[k v] params]
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

(defn merge
  "Merge query strings or query maps. Result is a map, and follows the
  same logic as the clojure merge, just decodes query strings into
  maps on the way."
  [& queries]
  (apply clojure.core/merge (map decode queries)))

(defn php-flatten
  "Flattens a map and formats the keys using the same style as PHP
  expects, allowing sending dictionaries and arrays via query encoding."
  [params]
  (letfn [(mk [k p] (if (blank? p)
                      (as-str k)
                      (str (as-str p) "[" (as-str k) "]")))
          (re [k v p] (if (associative? v) (tr v (mk k p)) {(mk k p) v}))
          (tr [params p] (apply merge (map (fn [[k v]] (re k v p)) params)))]
    (tr params nil)))

(defn underscore-keys
  "Replaces all dashes in key names with underscore without any regard
  for your sanity."
  [params]
  (letfn [(rename-key [k] (str-replace (as-str k) "-" "_"))
          (process-value [v] (if (associative? v) (underscore-keys v) v))]
    (into {} (map (fn [[k v]] [(rename-key k) (process-value v)]) params))))
