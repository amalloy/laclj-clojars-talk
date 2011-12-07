!SLIDE code
# Using your library - project.clj
(defproject my-awesome-project "0.5.0"
  :dependencies [[clojure "1.3.0"]
                 [YOURNAME-utils "0.2.3"]])

!SLIDE code
# Using your library - source files

(ns my.awesome.project
  (:use [YOURNAME.utils :only [records-matching]]))

;; Much nicer, right?
(defn old-peoples-names [people]
  (records-matching :age #(> % 80)
                    [:first-name :last-name]
                    people))

!SLIDE bullets
# Seriously, that's it. You're on Clojars now. #
