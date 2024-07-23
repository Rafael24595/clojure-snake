(ns clojure-snake.core-test
  (:require [clojure.test :refer :all]
            [clojure-snake.core :refer :all]))

(def snake-len-1 [{:x 5 :y 5}])
(def snake-len-2 [{:x 5 :y 5} {:x 5 :y 6}])
(def snake-len-3 [{:x 5 :y 5} {:x 5 :y 6} {:x 4 :y 6}])

(deftest a-test
  (testing "Snake with len 1 move"
    (let [update (move-snake {:x 1 :y 0} snake-len-1)]
      (is (= 6 (:x (get update 0)))) (is (= 5 (:y (get update 0)))))))

(deftest a-test
  (testing "Snake with len 2 move"
    (let [update (move-snake {:x 1 :y 0} snake-len-2)]
      (is (= 6 (:x (get update 0)))) (is (= 5 (:y (get update 0))))
      (is (= 5 (:x (get update 1)))) (is (= 5 (:y (get update 1)))))))

(deftest a-test
  (testing "Snake with len 3 move"
    (let [update (move-snake {:x 1 :y 0} snake-len-3)]
      (is (= 6 (:x (get update 0)))) (is (= 5 (:y (get update 0))))
      (is (= 5 (:x (get update 1)))) (is (= 5 (:y (get update 1))))
      (is (= 5 (:x (get update 2)))) (is (= 6 (:y (get update 2)))))))