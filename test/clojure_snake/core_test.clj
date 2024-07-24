(ns clojure-snake.core-test
  (:require [clojure.test :refer :all]
            [clojure-snake.core :refer :all]))

(def snake-len-1 [{:x 5 :y 5}])
(def snake-len-2 [{:x 5 :y 5} {:x 5 :y 6}])
(def snake-len-3 [{:x 5 :y 5} {:x 5 :y 6} {:x 4 :y 6}])
(def snake-len-4 [{:x 19 :y 0} {:x 18 :y 0} {:x 17 :y 0} {:x 16 :y 0}])
(def snake-len-5 [{:x 5 :y 5} {:x 4 :y 5} {:x 4 :y 4} {:x 5 :y 4} {:x 3 :y 4}])

(def test-state-non-collide
  {:snake snake-len-5
   :direction {:x 0 :y 1}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(def test-state-collide-self
  {:snake snake-len-5
   :direction {:x 0 :y -1}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(def test-state-collide-wall-x
  {:snake snake-len-4
   :direction {:x 1 :y 0}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(def test-state-collide-wall-y
  {:snake snake-len-4
   :direction {:x 0 :y -1}
   :food {:x 10 :y 10}
   :width 20
   :height 20
   :score 0})

(deftest test-move-1
  (testing "Snake with len 1 move"
    (let [update (move-snake {:x 1 :y 0} snake-len-1)]
      (is (= 6 (:x (get update 0)))) (is (= 5 (:y (get update 0)))))))

(deftest test-move-2
  (testing "Snake with len 2 move"
    (let [update (move-snake {:x -1 :y 0} snake-len-2)]
      (is (= 4 (:x (get update 0)))) (is (= 5 (:y (get update 0))))
      (is (= 5 (:x (get update 1)))) (is (= 5 (:y (get update 1)))))))

(deftest test-move-3
  (testing "Snake with len 3 move"
    (let [update (move-snake {:x 0 :y 1} snake-len-3)]
      (is (= 5 (:x (get update 0)))) (is (= 6 (:y (get update 0))))
      (is (= 5 (:x (get update 1)))) (is (= 5 (:y (get update 1))))
      (is (= 5 (:x (get update 2)))) (is (= 6 (:y (get update 2)))))))

(deftest test-move-4
  (testing "Snake with len 3 move"
    (let [update (move-snake {:x 0 :y -1} snake-len-3)]
      (is (= 5 (:x (get update 0)))) (is (= 4 (:y (get update 0))))
      (is (= 5 (:x (get update 1)))) (is (= 5 (:y (get update 1))))
      (is (= 5 (:x (get update 2)))) (is (= 6 (:y (get update 2)))))))

(deftest test-non-collision
  (testing "Snake non collision"
    (let [has-collide? (collision? test-state-non-collide)]
      (is (false? has-collide?)))))

(deftest test-collision-self
  (testing "Snake self collision"
    (let [has-collide? (collision? test-state-collide-self)]
      (is (true? has-collide?)))))

(deftest test-collision-wall-x
  (testing "Snake X wall collision"
    (let [has-collide? (collision? test-state-collide-wall-x)]
      (is (true? has-collide?)))))

(deftest test-collision-wall-y
  (testing "Snake Y wall collision"
    (let [has-collide? (collision? test-state-collide-wall-y)]
      (is (true? has-collide?)))))

(deftest test-update-state-non-collide
  (testing "Update state non collide"
    (let [update (update-state test-state-non-collide)
          snake (:snake update)]
      (is (= 5 (:x (get snake 0)))) (is (= 6 (:y (get snake 0)))))))

(deftest test-update-state-collide-self
  (testing "Update state collide self"
    (let [update (update-state test-state-collide-self)
          snake (:snake update)]
      (is (= 5 (:x (get snake 0)))) (is (= 5 (:y (get snake 0)))))))