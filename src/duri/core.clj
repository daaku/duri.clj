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
  (into {} (filter (comp some? val) (first (diff data defaults)))))

(defn- path-or-default
  "Returns the path, or a default path of \"/\" if the given one is blank."
  [path]
  (if (blank? path)
    "/"
    path))

(defmacro if-not-empty
  "Use a value if it isn't empty, else use the other value."
  [val & [else]]
  `(let [val# ~val
         else# ~else]
     (cond
       (nil? val#) else#

       (string? val#)
       (if (blank? val#)
         else#
         val#)

       (seq? val#)
       (if (empty? val#)
         else#
         val#)

       :else val#)))

(defn parse
  "Parse a URL string into it's parts."
  [input]
  (if (map? input)
    input
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
        :query {}}))))

(defn build
  "Builds a URL string from it's parts."
  [parts]
  (if (string? parts)
    parts
    (str (URI.
          (if-not-empty (parts :scheme))
          (if-not-empty (parts :user-info))
          (if-not-empty (parts :host))
          (if-not-empty (parts :port) -1)
          (path-or-default (parts :path))
          (if-not-empty (qs/encode (parts :query)))
          (if-not-empty (parts :fragment))))))

(defn append-query
  "Append some query data to the given thing. The given thing may be a
  string URL or a map of it's parts."
  [uri query]
  (let [uri (parse uri)]
    (assoc uri :query (qs/merge (uri :query) query))))
