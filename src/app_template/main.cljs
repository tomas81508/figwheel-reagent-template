(ns app-template.main
  (:require [reagent.core :as reagent]
            [app-template.view.app :refer [app-component]]))

(enable-console-print!)

(defonce app-state-atom (atom nil))

(defn handle-event!
  [{name :name
    data :data}]
  (condp = name
    :search-value-changed
    (swap! app-state-atom assoc :search-value (:value data))))

(defn render
  [app-state]
  (reagent/render-component [app-component {:app-state     app-state
                                            :trigger-event handle-event!}]
                            (js/document.getElementById "app")))

(when (nil? (deref app-state-atom))
  (add-watch app-state-atom
             :game-loop
             (fn [_ _ old-value new-value]
               (render new-value)))

  (reset! app-state-atom {:text         "Hello world!"
                          :search-value ""
                          :values       ["Morgan"
                                         "Tomas"
                                         "Daniel"
                                         "Anthony"
                                         "Josefin"
                                         "Annsofi"
                                         "Simon"
                                         "Superman"]}))

(defn on-js-reload []
  (render (deref app-state-atom)))
