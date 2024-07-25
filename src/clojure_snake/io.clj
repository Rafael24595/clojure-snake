(ns clojure-snake.io 
  (:require [clojure-snake.logic.entities :refer [new-game]]
            [clojure-snake.logic.game :refer [is-possible-turn? update-state]]
            [quil.core :as q]
            [quil.middleware :as m]))

(def ^:private mult 20)

(defn ^:private draw-background []
  (q/background 255))

(defn ^:private draw-snake [state]
  (q/fill 0)
  (doseq [{:keys [x y]} (:snake state)]
    (q/rect (* x mult) (* y mult) mult mult)))

(defn ^:private draw-fruit [state]
  (q/fill 255 0 0)
  (let [{:keys [x y]} (:food state)]
    (q/rect (* x mult) (* y mult) mult mult)))

(defn ^:private draw [state]
  (draw-background)
  (draw-snake state)
  (draw-fruit state))

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
             (* (:height state) mult)]
      :setup #(setup state)
      :draw draw
      :update update-state
      :key-pressed key-pressed
      :middleware [m/fun-mode])))
