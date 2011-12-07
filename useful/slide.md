!SLIDE bullets
# Useful

* Geni's open-source utilities
* Use it, or fork it, if you want
* Send pull requests if you want, but we won't take most of them - your utility library is *yours*

!SLIDE bullets
# A tour

* Samples of some things in Useful
* Hoping to inspire you to write something better
* Stop me if you get bored

!SLIDE code
    (some #(= 4 (/ % 9)) (range)) ; true
    (find-first #(= 4 (/ % 9)) (range)) ; 36

!SLIDE code
    (let [x 4]
      (+ 9 (? (* x 2))))
    ;; stdout: "(* x 2) is 8"
    ;; returns: 17

!SLIDE code
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
    (fix 3
         string? keyword ; fall through
         number? #(* 2 %)) ; return (* 2 x)

!SLIDE code
    (let [counter (atom 0)]
      (def thread-num (thread-local
                       (swap! counter inc))))
    @thread-num ;; 1
    @(future @thread-num) ;; 2
    @thread-num ;; still 1

!SLIDE code
    (let [age 26, nick "amalloy"]
      [(keyed [age nick])
       (keyed :strs [age nick])])

    ;; [{:age 26, :nick "amalloy"},
    ;;  {"age" 26, "nick" "amalloy"}]

!SLIDE code
    (into {}
          (map (knit keyword int)
               {"x" 1.0, "y" 7.0}))
    ;; {:x 1, :y 7}

!SLIDE code
    (let [m {:id "34",
             :max "105",
             :data {:foo :bar}}]
      (update-each m [:id :max] read-string))

    ;; {:id 34, :max 105, :data {:foo :bar}}
