(ns clojure-snake.core 
  (:require [clojure-snake.io :refer [run]]
            [clojure-snake.logic.entities :refer [new-game]]))

(defn -main [& args]
  (let [state (new-game)]
    (run state)))