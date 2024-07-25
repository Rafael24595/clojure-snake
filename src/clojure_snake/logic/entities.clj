(ns clojure-snake.logic.entities)

(def ^:private initial-state
  {:snake [{:x 5 :y 5} {:x 4 :y 5} {:x 3 :y 5}]
   :direction {:x 0 :y 0}
   :food {:x 0 :y 0}
   :width 20
   :height 20
   :score 0})

(defn new-fruit [state]
  (if (>= (count (:snake state)) (* (:width state (:height state))))
    ()
    (let [new-fruit {:x (Math/round (rand (:width state)))
                     :y (Math/round (rand (:height state)))}]
      (if (some #(= new-fruit %) (rest (:snake state)))
        (recur state)
        (assoc state :food new-fruit))
      )))

(defn new-game []
  (new-fruit initial-state))