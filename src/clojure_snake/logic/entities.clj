(ns clojure-snake.logic.entities)

(def ^:private initial-state
  {:snake [{:x 10 :y 0}]
   :direction {:x 0 :y 1}
   :food {:x 0 :y 0}
   :width 21
   :height 21
   ;time 0
   :status "NEW_GAME"
   :score 0})

(defn win-game [state]
  (assoc state :status "WIN_GAME"))

(defn game-over [state]
  (assoc state :status "GAME_OVER"))

(defn launch-game [state]
  (assoc state :status "RUNNING"))

(defn ^:private new-fruit [state]
  (if (>= (count (:snake state)) (* (:width state) (:height state)))
    (win-game state)
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
    (win-game state)
    (let [tail (find-valid-tail state)] 
      (if (nil? tail)
        (game-over state); TODO: Manage condition
        (assoc state :snake (conj (:snake state) tail))))))

(defn ^:private increment-score [state]
  (let [score (:score state)]
    (assoc state :score (+ score 100))))

(defn eat-fruit [state]
  (new-fruit (increment-score (grow state))))

(defn new-game []
  (new-fruit initial-state))