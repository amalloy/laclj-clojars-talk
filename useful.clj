;;; find-first
(some       #(= 4 (/ % 9)) (range)) ;; true
(find-first #(= 4 (/ % 9)) (range)) ;; 36

;;; ?
(let [x 4]
  (+ 9 (? (* x 2))))
;; stdout: "(* x 2) is 8"
;; returns: 17
;; note cake plugin/option

;;; lazy-loop
;; like loop/recur, but don't have to be in tail position (lazy-seqs only)
(defn range [max]
  (lazy-loop [n 0]
    (when-not (= n max)
      (cons n
            (lazy-recur (inc n))))))
;; compare using lazy-seq directly - cleaner flow, don't "invent" range*
(defn range [max]
  ((fn range* [n]
     (lazy-seq
      (when-not (= n max)
        (cons n
              (range* (inc n))))))
   0))

;;; fix
(fix 3
     string? keyword   ;; clause doesn't match: fall through
     number? #(* 2 %)) ;; clause matches: return (* 2 x) = 6

;; to-fix, given: "companion" functions
;; note fix matches unified update model - can chain with alter, update-in, etc

;;; thread-local
(let [counter (atom 0)]
  (def thread-num (thread-local
                   (swap! counter inc))))
@thread-num ;; 1
@(future [@thread-num @thread-num]) ;; [2 2]
@thread-num ;; still 1

;;; keyed
;; opposite of map destructuring
(let [age 26, nick "amalloy"]
  [(keyed [age nick])          ;; {:age 26, :nick "amalloy"}
   (keyed :strs [age nick])])  ;; {"age" 26, "nick" "amalloy"}

;;; knit
;; sibling to juxt, applies one function to each arg instead of each fn to all args
(let [input-map {"x" 1.0, "y" 7.0}]
 (into {}
       (map (knit keyword int) input-map)))
;; {:x 1, :y 7}

;;; update-each
(let [m {:id "34", :max "105", :data {:foo :bar}}]
  (update-each m [:id :max] read-string)) ;; {:id 34, :max 105, :data {:foo :bar}}
