(ns app-template.view.app)

(defn js-event->value
  [event]
  (-> event
      (.-target)
      (.-value)))

(defn app-component
  "The main component."
  [{app-state     :app-state
    trigger-event :trigger-event}]
  [:div
   [:h1 (:text app-state)]
   [:input {:placeholder "Sök"
            :style       {:outline "1px solid orange"}
            :on-change   (fn [e]
                           (trigger-event {:name :search-value-changed
                                           :data {:value (js-event->value e)}}))
            :value       (:search-value app-state)}]
   [:div
    (let [search-value (clojure.string/lower-case (:search-value app-state))]
      (->> (:values app-state)
           (filter (fn [value]
                     (clojure.string/starts-with? (clojure.string/lower-case value)
                                                  search-value)))
           (map (fn [value]
                  [:div {:key   value
                         :style {:font-size "10px"}}
                   value]))))]])

