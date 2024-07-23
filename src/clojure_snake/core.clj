(ns clojure-snake.core)

(def initial-state
  {:snake [{:x 5 :y 5}]
   :direction {:x 0 :y 0}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(defn move-head [direction, head]
  {:x (+ (:x head) (:x direction))
   :y (+ (:y head) (:y direction))})

(defn move-snake 
  ([state]
   (let [direction (:direction state)
         snake (:snake state)]
     (assoc state :snake (move-snake direction snake))))
  ([direction, snake]
   (loop [i 0
          new-snake []]
     (if (< i (count snake))
       (if (= i 0)
         (recur (inc i) (conj new-snake (move-head direction, (get snake i))))
         (recur (inc i) (conj new-snake (get snake (dec i)))))
       (vec new-snake)))))

(defn -main []
  (println "Hello Clojure!"))