!SLIDE bullets
# Useful

* Geni's open-source utilities
* Use it, or fork it, if you want
* Send pull requests if you want
* We won't take most of them - your utility library is *yours*

!SLIDE bullets
# A tour

* Samples of some things in Useful
* Hoping to inspire you to write something better
* Stop me if you get bored

!SLIDE code
# find-first

    (some       #(= 4 (/ % 9)) (range)) ;; true
    (find-first #(= 4 (/ % 9)) (range)) ;; 36

!SLIDE code
# ?

    (let [x 4]
      (+ 9 (? (* x 2))))
    ;; stdout: "(* x 2) is 8"
    ;; returns: 17

!SLIDE code
# lazy-loop

    (defn range [max]
      (lazy-loop [n 0]
        (when-not (= n max)
          (cons n
                (lazy-recur (inc n))))))

    (defn range [max]
      ((fn range* [n]
         (lazy-seq
          (when-not (= n max)
            (cons n
                  (range* (inc n))))))
       0))

!SLIDE code
# fix

    (fix 3
         string? keyword ;; no match: fall through
         number? #(* 2 %)) ;; match: return (* 2 x) = 6

!SLIDE code
# thread-local

    (let [counter (atom 0)]
      (def thread-num (thread-local
                       (swap! counter inc))))
    @thread-num ;; 1
    @(future [@thread-num @thread-num]) ;; [2 2]
    @thread-num ;; still 1

!SLIDE code
# keyed

    (let [age 26, nick "amalloy"]
      [(keyed [age nick])
       (keyed :strs [age nick])])

    ;; [{:age 26, :nick "amalloy"},
    ;;  {"age" 26, "nick" "amalloy"}]

!SLIDE code
# knit
    (let [input-map {"x" 1.0, "y" 7.0}]
      (into {}
            (map (knit keyword int) input-map)))
    ;; {:x 1, :y 7}

!SLIDE code
# update-each

    (let [m {:id "34",
             :max "105",
             :data {:foo :bar}}]
      (update-each m [:id :max] read-string))

    ;; {:id 34, :max 105, :data {:foo :bar}}
