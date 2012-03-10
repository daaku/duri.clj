(ns duri.core
  "URI parsing."
  {:author "Naitik Shah"}
  (:require [duri.qs :as qs])
  (:use [clojure.core.incubator :only [-?>]]
        [clojure.data :only [diff]]
        [clojure.string :only [blank?]])
  (:import [java.net URI]))

(defn- remove-defaults-and-nil
  "Removes the given default values and nil values from the data."
  [data defaults]
  (first (diff data defaults)))

(defn- path-or-default
  "Returns the path, or a default path of \"/\" if the given one is blank."
  [path]
  (if (blank? path)
    "/"
    path))

(defn- normalize-qs
  "Encodes a map to a query string if necessary, or returns strings as-is."
  [query]
  (if (map? query)
    (qs/encode query)
    query))

(defn parse
  "Parse a URL string into it's parts."
  [input]
  (let [uri (URI. input)]
    (remove-defaults-and-nil
     {:scheme (-?> uri .getScheme .toLowerCase)
      :user-info (.getUserInfo uri)
      :host (-?> uri .getHost .toLowerCase)
      :port (.getPort uri)
      :path (path-or-default (.getPath uri))
      :query (qs/decode (.getQuery uri))
      :fragment (.getFragment uri)}
     {:port -1
      :query {}})))

(defn build
  "Builds a URL string from it's parts."
  [parts]
  (.toString
   (URI.
    (get parts :scheme)
    (get parts :user-info)
    (get parts :host)
    (get parts :port -1)
    (get parts :path "/")
    (normalize-qs (get parts :query))
    (get parts :fragment))))
