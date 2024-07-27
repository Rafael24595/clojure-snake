(ns clojure-snake.io 
  (:require [clojure-snake.logic.entities :refer [launch-game new-game]]
            [clojure-snake.logic.game :refer [is-possible-turn? update-state]]
            [quil.core :as q]
            [quil.middleware :as m]))

(def ^:private mult 20)

(def ^:private data-box-color [210 165 120])
(def ^:private data-box-text-color 245)

(def ^:private snake-head-color [20 155 0])
(def ^:private snake-body-even-color [20 220 0])
(def ^:private snake-body-odd-color [20 205 0])

(def ^:private food-color [255 0 0])

(defn ^:private draw-background []
  (q/background 250 240 215)
  
 (let [box-height (* mult 2)
       box-width (q/width)
       y-pos (- (q/height) box-height 0)] 
   (q/stroke 45)
   (q/stroke-weight 2)
 
   (q/fill data-box-color)
   (q/rect 0 y-pos box-width box-height)

   (q/stroke 0) 
   (q/stroke-weight 1)))

(defn draw-box-with-message [state, message]
  (let [screen-width (q/width)
        screen-height (q/height)
        box-width 200
        box-height 100
        box-x (/ (- screen-width box-width) 2)
        box-y (/ (- screen-height (+ box-height (* mult 2))) 2)] 
    
    (q/fill data-box-color)
    (q/rect box-x box-y box-width box-height) 

    (q/fill data-box-text-color)
    (q/text-align :center :center)
    (q/text message (+ box-x (/ box-width 2)) (+ box-y (/ box-height 2)))
    (q/text-align :left))
  state)

(defn ^:private draw-snake-head [state]
  (q/fill snake-head-color)
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
            (q/fill snake-body-even-color)
            (q/fill snake-body-odd-color)) 
          (q/rect (* x mult) (* y mult) mult mult) 
          (recur (inc i) (rest remaining-fragments))))))
  (q/stroke 0)
  (q/stroke-weight 1))

(defn ^:private draw-food [state]
  (q/stroke 55 20 0)
  (q/stroke-weight 1)
  (q/fill food-color)
  (let [{:keys [x y]} (:food state)]
    (q/rect (* x mult) (* y mult) mult mult))
  (q/stroke 0)
  (q/stroke-weight 1))

(defn ^:private draw-score [state] 
  (q/text-size 16)
  (q/fill data-box-text-color)
  (q/text-align :left :center)
  (q/text (str "Score: " (:score state)) (/ mult 2) (- (q/height) mult))
  (q/text-align :left))

(defn draw-game-new [state]
  (draw-box-with-message state "NEW GAME")
  (draw-score state))

(defn draw-game-run [state]
  (draw-snake state)
  (draw-food state)
  (draw-score state))

(defn draw-game-over [state]
  (draw-box-with-message state "GAME OVER")
  (draw-score state))

(defn draw-game-win [state]
  (draw-box-with-message state "GAME WIN!")
  (draw-score state))

(defn ^:private draw [state]
  (draw-background)
  (let [status (:status state)] 
    (cond 
      (= status "NEW_GAME") (draw-game-new state)
      (= status "RUNNING") (draw-game-run state)
      (= status "GAME_OVER") (draw-game-over state)
      (= status "WIN_GAME") (draw-game-win state)
      :else state)))

(defn ^:private key-pressed-running [state key]
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

(defn ^:private key-pressed [state key] 
  (let [status (:status state)] 
    (cond
      (= status "NEW_GAME") (launch-game state)
      (= status "RUNNING") (key-pressed-running state key)
      (= status "GAME_OVER") (launch-game (new-game))
      (= status "WIN_GAME") (launch-game (new-game))
      :else state)))

(defn ^:private setup [state]
  (q/frame-rate 10)
  state)

(def ^:private sketch-name 'clojure-snake)

(defn run [state]
  (q/defsketch sketch-name
    :title "Clojure Snake"
    :size [(* (:width state) mult)
           (+ (* (:height state) mult) (* mult 2))]
    :setup #(setup state)
    :draw draw
    :update update-state
    :key-pressed key-pressed
    :middleware [m/fun-mode]))
