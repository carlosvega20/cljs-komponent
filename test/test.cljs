(ns test.core
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [komponent.core :as komponent]))

(defn run [] (bound-records-logic))

(deftest bound-records-logic "Test bound records logic"
(let [records (vec (range 10))
      offset 3
      limit 2
      br (komponent/bound-records records offset limit)]
  (testing "Result should be bound as expected"
    (is (= br '(6 7)))
  (testing "Result should not be empty"
    (is (not (empty? br))))
  (testing "Records length should correspond"
    (is (= (count br) limit ))))
))
