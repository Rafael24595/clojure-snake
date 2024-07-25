(ns clojure-snake.logic.entity-test
  (:require [clojure.test :refer :all]
            [clojure-snake.logic.entities :refer :all]))

(def test-state-grow-conflict-1
  {:snake [{:x 5 :y 5} {:x 4 :y 5} {:x 4 :y 4} {:x 5 :y 4}]
   :direction {:x 1 :y 0}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(def test-state-grow-conflict-2
  {:snake [{:x 5 :y 5} {:x 4 :y 5} {:x 4 :y 4} {:x 5 :y 4}]
   :direction {:x 0 :y 1}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(def test-state-grow-conflict-3
  {:snake [{:x 5 :y 5} {:x 4 :y 5} {:x 4 :y 4} {:x 5 :y 4}]
   :direction {:x 0 :y -1}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(deftest test-grow-conflict-1
  (testing "Valide grow conflict"
    (let [snake (:snake (eat-fruit test-state-grow-conflict-1))]
      (is (= 6 (:x (last snake)))) (is (= 4 (:y (last snake)))))))

(deftest test-grow-conflict-2
  (testing "Valide grow conflict"
    (let [snake (:snake (eat-fruit test-state-grow-conflict-2))]
      (is (= 5 (:x (last snake)))) (is (= 3 (:y (last snake)))))))

(deftest test-grow-conflict-3
  (testing "Valide grow conflict"
    (let [snake (:snake (eat-fruit test-state-grow-conflict-3))]
      (is (= 5 (:x (last snake)))) (is (= 3 (:y (last snake)))))))

(deftest test-increment-score
  (testing "Valid increment score"
    (let [final-state (reduce (fn [state _] (eat-fruit state )) test-state-grow-conflict-3 (range 3))
          score (:score final-state)]
      (is (= 300 score)))))