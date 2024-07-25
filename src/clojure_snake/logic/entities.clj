(ns clojure-snake.logic.entities)

(def initial-state
  {:snake [{:x 5 :y 5} {:x 4 :y 5} {:x 3 :y 5}]
   :direction {:x 0 :y 0}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})