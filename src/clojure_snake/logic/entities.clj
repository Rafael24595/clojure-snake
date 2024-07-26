(ns clojure-snake.logic.entities)

(def ^:private initial-state
  {:snake [{:x 5 :y 5} {:x 4 :y 5} {:x 3 :y 5} {:x 3 :y 4} {:x 3 :y 3} {:x 3 :y 2} {:x 3 :y 1} {:x 4 :y 1} {:x 5 :y 1} {:x 6 :y 1} {:x 7 :y 1} {:x 8 :y 1} {:x 9 :y 1} {:x 10 :y 1} {:x 11 :y 1} {:x 12 :y 1} {:x 13 :y 1} {:x 14 :y 1}]
   :direction {:x 0 :y 0}
   :food {:x 0 :y 0}
   :width 20
   :height 20
   ;time 0
   ;status "STARTED"
   :score 0})

(defn ^:private new-fruit [state]
  (if (>= (count (:snake state)) (* (:width state) (:height state)))
    (println "new-fruit error" (str (count (:snake state))) " - " (str (* (:width state) (:height state)))); TODO: Manage condition
    (let [new-fruit {:x (Math/round (inc (rand (- (:width state) 2))))
                     :y (Math/round (inc (rand (- (:height state) 2))))}]
      (if (some #(= new-fruit %) (rest (:snake state)))
        (recur state)
        (assoc state :food new-fruit))
      )))

(defn ^:private make-positions [direction tail]
  (let [x (:x tail)
        y (:y tail)]
     (cond
       (= (:x direction) 1) [{:x (- x 1) :y y} {:x (+ x 1) :y y} {:x x :y (- y 1)} {:x x :y (+ y 1)}] 
       (= (:x direction) -1) [{:x (+ x 1) :y y} {:x (- x 1) :y y} {:x x :y (+ y 1)} {:x x :y (- y 1)}]
       (= (:y direction) 1) [{:x x :y (- y 1)} {:x x :y (+ y 1)} {:x (- x 1) :y y} {:x (+ x 1) :y y}]
       :else [{:x x :y (+ y 1)} {:x x :y (- y 1)} {:x (+ x 1) :y y} {:x (- x 1) :y y}])))

(defn ^:private find-valid-tail [state]
  (let [posibilities (make-positions (:direction state) (last (:snake state)))]
    (loop [i 0]
      (if (< i (count posibilities))
        (let [position (get posibilities i)]
          (if (some #(= position %) (:snake state))
            (recur (inc i))
            position))
        nil))))

(defn ^:private grow [state]
  (if (>= (count (:snake state)) (* (:width state) (:height state)))
    (println "grow error"); TODO: Manage condition
    (let [tail (find-valid-tail state)] 
      (if (nil? tail)
        (println "nil tail error"); TODO: Manage condition
        (assoc state :snake (conj (:snake state) tail))))))

(defn ^:private increment-score [state]
  (let [score (:score state)]
    (assoc state :score (+ score 100))))

(defn eat-fruit [state]
  (new-fruit (increment-score (grow state))))

(defn new-game []
  (new-fruit initial-state))