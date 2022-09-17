(ns template.view.app
  (:require [clojure.string :as str]
            [template.view.clock :refer [analogue-clock]]))

; util
(defn js-event->value
  [event]
  (-> event
      (.-target)
      (.-value)))


(defn greeting [message]
  [:h1 {:style {:color "#777"}}
   message])

(defn digital-clock
  [app-state-atom]
  (let [app-state (deref app-state-atom)
        time-str (-> (:time app-state)
                     (.toTimeString)
                     (str/split " ")
                     (first))]
    [:div
     {:style {:color       (get-in app-state [:style :time-color])
              :font-size   "128px"
              :font-family "HelveticaNeue-UltraLight, Helvetica;"}}
     time-str]))

(defn color-input
  [app-state-atom trigger-event]
  (let [app-state (deref app-state-atom)]
    [:div {:style {:font-size   "24px;"
                   :line-height "1.5em;"}}
     "Time color: "
     [:input {:type      "text"
              :value     (get-in app-state [:style :time-color])
              :on-change (fn [e]
                           (trigger-event {:name :time-color-changed
                                           :data (js-event->value e)}))}]]))


(defn app-component
  "The main component."
  [{app-state-atom :app-state-atom
    trigger-event  :trigger-event}]
  [:div {:style {:font-family "HelveticaNeue, Helvetica;"}}
   [greeting "Hello world, it is now"]
   [digital-clock app-state-atom]
   [color-input app-state-atom trigger-event]
   [:div {:style {:padding-top "20px"}}
    [analogue-clock app-state-atom 400]]])

