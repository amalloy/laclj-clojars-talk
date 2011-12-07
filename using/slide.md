!SLIDE code
# Using - project.clj
    (defproject my-awesome-project "0.5.0"
      :dependencies [[clojure "1.3.0"]
                     [NAME-utils "0.2.3"]])

!SLIDE code
# Using - source files

    (ns my.awesome.project
      (:use [NAME.utils :only [records-matching]]))

    (defn old-peoples-names [people]
      (records-matching :age #(> % 80)
                        [:first-name :last-name]
                        people))

!SLIDE bullets
# Seriously, that's it. You're on Clojars now. #
