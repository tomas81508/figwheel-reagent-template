(ns ^:figwheel-hooks template.main
  (:require [reagent.core :as reagent]
            [reagent.dom :as reagent-dom]
            [template.view.app :refer [app-component]]))

(enable-console-print!)

(defonce app-state-atom
         (reagent/atom {:time  (js/Date.)
                        :style {:time-color "#f34"}}))

(defn handle-event!
  [{name :name
    data :data}]
  (condp = name
    :time-color-changed
    (swap! app-state-atom assoc-in [:style :time-color] data)))

(defn render
  {:export true}
  []
  (println "Rendering!")
  (reagent-dom/render [app-component {:app-state-atom app-state-atom
                                      :trigger-event  handle-event!}]
                      (js/document.getElementById "app")))


; A function that will run only once even under dev hot reload
(defonce init (do (js/setInterval (fn []
                                    (swap! app-state-atom assoc :time (js/Date.)))
                                  1000)
                  (render)))

(defn on-js-reload
  {:after-load true}
  []
  (println "Reloading!!!")
  (render))
