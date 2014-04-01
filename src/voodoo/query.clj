(ns voodoo.query
  (:require [clojure.string :as str]))

(defn- spaces
  [operator]
  (if (or (= (name operator) "AND") (= (name operator) "OR"))
    (str " ")
    (str "")))

(defn expand-expr 
  [expr]
  (if (coll? expr)
    (if (= (first expr) `unquote)
      "?"
      (let [[op & args] expr]
        (str (str/join (str (spaces op) op (spaces op)) (map expand-expr args)))))
    expr))

(declare expand-clause)

(def clause-map
  {'WHERE (fn [expr] (str (expand-expr expr)))})

(defn expand-clause 
  [[op & args]]
  (apply (clause-map op) args))

(defmacro WHERE 
  [& args]
  [(expand-clause (cons 'WHERE args))
   (vec (for [n (tree-seq coll? seq args) :when (and (coll? n) (= (first n) `unquote))] (second n)))])

(defn- quote-if-string
  [value]
  (if (instance? String value) 
    (str "'" value "'")
    value))

(defn get-ooyala-query
  [query-with-clauses]
  (reduce (fn [query-string clause] (str/replace-first query-string "?" clause)) 
            (first query-with-clauses) (map quote-if-string (second query-with-clauses))))