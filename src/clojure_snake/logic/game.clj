(ns clojure-snake.logic.game 
  (:require [clojure-snake.logic.entities :refer [eat-food game-over]]))

(defn ^:private move-head [direction, head]
  {:x (+ (:x head) (:x direction))
   :y (+ (:y head) (:y direction))})

(defn move-snake
  ([state]
   (let [direction (:direction state)
         snake (:snake state)]
     (assoc state :snake (move-snake direction snake))))
  ([direction, snake]
   (if (and (= (:x direction) 0) (= (:y direction) 0))
     snake
     (loop [i 0
            new-snake []]
       (if (< i (count snake))
         (if (= i 0)
           (recur (inc i) (conj new-snake (move-head direction, (get snake i))))
           (recur (inc i) (conj new-snake (get snake (dec i)))))
         (vec new-snake))))))

(defn collision? [state]
  (let [head (move-head (:direction state) (first (:snake state)))
        collide-wall? (or (< (:x head) 0) (>= (:x head) (:width state))
                          (< (:y head) 0) (>= (:y head) (:height state)))
        collide-self? (some #(= head %) (rest (:snake state)))]
    (boolean (or collide-wall? collide-self?))))

(defn food-colide? [state]
  (let [food (:food state)
        head (first (:snake state))] 
    (and (= (:x food) (:x head)) (= (:y food) (:y head)))))

(defn update-state [state]
  (if (= (:status state) "RUNNING")
    (if (collision? state)
      (game-over state)
      (let [update (move-snake state)]
        (if (food-colide? state)
          (eat-food update)
          update)))
    state))

(defn is-possible-turn? [a b]
  (not (and (not= a 0) (not= b 0) (= (+ a b) 0))))