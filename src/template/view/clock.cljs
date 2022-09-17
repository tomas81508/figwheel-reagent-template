(ns template.view.clock
  (:require [reagent.core :as reagent]))


(def angle-diff-hour 30)
(def angle-diff-minutes (/ 1 2))
(def angle-diff 6)

(defn get-time [date-obj]
  {:hour    (.getHours date-obj)
   :minutes (.getMinutes date-obj)
   :seconds (.getSeconds date-obj)})

(defn hour-component
  [{size    :size
    minutes :minutes
    hour    :hour}]
  (let [r (/ size 2)
        l (* 0.6 r)
        angle (+ (* minutes angle-diff-minutes) (* hour angle-diff-hour))]
    [:line {:x1    r
            :y1    (+ r (* size 0.03))
            :x2    r
            :y2    (- r l)
            :style {:transform        (str "rotateZ(" angle "deg)")
                    :transform-origin (str r "px " r "px")
                    :stroke           "black"
                    :stroke-width     (* size 0.025)}}]))

(defn minutes-component
  [{size    :size
    minutes :minutes}]
  (let [local-state-atom (reagent/atom {:animation true :angle-modification 0 :active false})]
    (fn [{size    :size
          minutes :minutes}]
      (let [local-state (deref local-state-atom)
            r (/ size 2)
            l (* 0.9 r)
            angle (* minutes angle-diff)
            animation-time 300
            angle-with-modification (+ angle (:angle-modification local-state))
            final-angle (if (< angle-with-modification -6)
                          (do (js/setTimeout (fn [] (swap! local-state-atom assoc
                                                           :angle-modification 0
                                                           :active false))
                                             animation-time)
                              angle)
                          angle-with-modification)]
        (when (and (= angle 354) (not (:active local-state)))
          (swap! local-state-atom assoc :active true)
          (js/setTimeout (fn []
                           (swap! local-state-atom assoc :animation false :angle-modification -360))
                         animation-time))
        (when-not (:animation local-state)
          (js/setTimeout (fn [] (swap! local-state-atom assoc :animation true))
                         100))
        [:line {:x1    r
                :y1    (+ r (* size 0.04))
                :x2    r
                :y2    (- r l)
                :style {:transform        (str "rotateZ(" final-angle "deg)")
                        :transform-origin (str r "px " r "px")
                        :transition       (when (:animation local-state)
                                            (str "transform " animation-time "ms ease"))
                        :stroke           "black"
                        :stroke-width     (* size 0.018)}}]))))

(defn seconds-component
  [{size    :size
    seconds :seconds}]
  (let [local-state-atom (reagent/atom {:animation true :angle-modification 0 :active false})]
    (fn [{size    :size
          seconds :seconds}]
      (let [local-state (deref local-state-atom)
            r (/ size 2)
            l (* 1 r)
            angle (* seconds angle-diff)
            animation-time 300
            angle-with-modification (+ angle (:angle-modification local-state))
            final-angle (if (< angle-with-modification -6)
                          (do (js/setTimeout (fn [] (swap! local-state-atom assoc
                                                           :angle-modification 0
                                                           :active false))
                                             animation-time)
                              angle)
                          angle-with-modification)]
        (when (and (= angle 354) (not (:active local-state)))
          (swap! local-state-atom assoc :active true)
          (js/setTimeout (fn []
                           (swap! local-state-atom assoc :animation false :angle-modification -360))
                         animation-time))
        (when-not (:animation local-state)
          (js/setTimeout (fn [] (swap! local-state-atom assoc :animation true))
                         100))
        [:line {:key   "seconds"
                :x1    r
                :y1    (+ r (* size 0.05))
                :x2    r
                :y2    (- r l)
                :style {:transform        (str "rotateZ(" final-angle "deg)")
                        :transform-origin (str r "px " r "px")
                        :transition       (when (:animation local-state)
                                            (str "transform " animation-time "ms ease"))
                        :stroke           "red"
                        :stroke-width     (* size 0.008)}}]))))


(defn analogue-clock
  [app-state-atom size]
  (let [app-state (deref app-state-atom)
        time (get-time (:time app-state))]
    [:svg {:viewBox (str "0 0 " size " " size)
           :style   {:width (str size "px")}}
     (let [angle-diff (/ js/Math.PI 30)
           r (/ size 2)]
       [:g
        (->> (range 60)
             (map (fn [i]
                    (let [angle (* i angle-diff)
                          major-node (zero? (mod i 5))
                          l (if major-node
                              (/ size 25)
                              (/ size 50))
                          x1 (* (js/Math.sin angle) r)
                          y1 (* (js/Math.cos angle) r)
                          x2 (* (js/Math.sin angle) (- r l))
                          y2 (* (js/Math.cos angle) (- r l))
                          stroke-width (if major-node
                                         (* size 0.015)
                                         (/ size 100))]
                      [:line {:key   i
                              :x1    (+ r x1)
                              :y1    (+ r y1)
                              :x2    (+ r x2)
                              :y2    (+ r y2)
                              :style {:stroke       "black"
                                      :stroke-width stroke-width}}]))))

        [hour-component {:size size :hour (:hour time) :minutes (:minutes time)}]
        [minutes-component {:size size :minutes (:minutes time)}]
        [seconds-component {:size size :seconds (:seconds time)}]

        [:circle {:cx r
                  :cy r
                  :r  (/ size 50)}]])]))