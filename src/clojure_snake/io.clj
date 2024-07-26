(ns clojure-snake.io 
  (:require [clojure-snake.logic.entities :refer [new-game]]
            [clojure-snake.logic.game :refer [is-possible-turn? update-state]]
            [quil.core :as q]
            [quil.middleware :as m]))

(def ^:private mult 20)

(defn ^:private draw-background []
  (q/background 250 240 215)
  
 (let [box-height (* mult 2)
       box-width (q/width)
       y-pos (- (q/height) box-height 0)] 
   (q/stroke 45)
   (q/stroke-weight 2)
 
   (q/fill 50, 75, 255)
   (q/rect 0 y-pos box-width box-height)

   (q/stroke 0) 
   (q/stroke-weight 1)))

(defn ^:private draw-snake-head [state]
  (q/fill 20 155 0)
  (let [{:keys [x y]} (first (:snake state))]
    (q/rect (* x mult) (* y mult) mult mult)))

(defn ^:private draw-snake [state]
  (q/stroke 20 55 0)
  (q/stroke-weight 1)
  (draw-snake-head state)
  (let [fragments (rest (:snake state))]
    (loop [i 0 remaining-fragments fragments]
      (when (seq remaining-fragments)
        (let [fragment (first remaining-fragments)
              x (:x fragment)
              y (:y fragment)] 
          (if (even? i) 
            (q/fill 20 220 0)
            (q/fill 20 205 0)) 
          (q/rect (* x mult) (* y mult) mult mult) 
          (recur (inc i) (rest remaining-fragments))))))
  (q/stroke 0)
  (q/stroke-weight 1))

(defn ^:private draw-fruit [state]
  (q/stroke 55 20 0)
  (q/stroke-weight 1)
  (q/fill 255 0 0)
  (let [{:keys [x y]} (:food state)]
    (q/rect (* x mult) (* y mult) mult mult))
  (q/stroke 0)
  (q/stroke-weight 1))

(defn ^:private draw-score [state] 
  (q/text-size 16)
  (q/fill 225)
  (q/text (str "Score: " (:score state)) 10 425))

(defn ^:private draw [state]
  (draw-background)
  (draw-snake state)
  (draw-fruit state)
  (draw-score state))

(defn ^:private key-pressed [state key]
  (let [old-direction (:direction state)
        new-direction (case (get key :key)
                        :left {:x -1 :y 0}
                        :right {:x 1 :y 0}
                        :up {:x 0 :y -1}
                        :down {:x 0 :y 1}
                        (:direction state))
        ox (:x old-direction)
        oy (:y old-direction)
        nx (:x new-direction)
        ny (:y new-direction)]
     (if (and (is-possible-turn? ox nx) (is-possible-turn? oy ny))
      (assoc state :direction new-direction)
      (assoc state :direction old-direction))))

(defn ^:private setup [state]
  (q/frame-rate 10)
  state)

(def ^:private sketch-name 'clojure-snake)

(defn run []
  (let [state (new-game)]
    (q/defsketch sketch-name
      :title "Snake Game"
      :size [(* (:width state) mult)
             (+ (* (:height state) mult) (* mult 2))]
      :setup #(setup state)
      :draw draw
      :update update-state
      :key-pressed key-pressed
      :middleware [m/fun-mode])))
