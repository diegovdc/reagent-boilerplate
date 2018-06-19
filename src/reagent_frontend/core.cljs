(ns reagent-frontend.core
    (:require-macros [secretary.core :refer [defroute]])
    (:import goog.history.Html5History
             goog.Uri)
    (:require
      [secretary.core :as secretary]
      [goog.events :as events]
      [goog.history.EventType :as EventType]
      [reagent.core :as r]))
;; -------------------------
;; History
(defn hook-browser-navigation! []
  (let [history (doto (Html5History.)
                  (events/listen
                   EventType/NAVIGATE
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
                  (.setUseFragment false)
                  (.setPathPrefix "")
                  (.setEnabled true))]

    (events/listen js/document "click"
                   (fn [e]
                     (. e preventDefault)
                     (let [path (.getPath (.parse Uri (.-href (.-target e))))
                           title (.-title (.-target e))]
                       (when path
                         (. history (setToken path title))))))))

(def app-state (r/atom {}))

;; -------------------------
;; Routes

(defn app-routes []
  ; (secretary/set-config! :prefix "")

  (defroute "/" []
    (swap! app-state assoc :page :home))

  (defroute "/about" []
    (swap! app-state assoc :page :about))

  (hook-browser-navigation!))

    ;; -------------------------
    
;; Views

(defn home []
  [:div [:h1 "Home Page"]
   [:a {:href "/about"} "about page"]])

(defn about []
  [:div 
   [:h1 "About Page"]
   [:a {:href "/"} "home page"]])

(defmulti current-page #(@app-state :page))
(defmethod current-page :home []
  [home])
(defmethod current-page :about []
  [about])
(defmethod current-page :default []
  [home])
;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [:div
             [:div "header"]
             [current-page]] 
            (.getElementById js/document "app")))

(defn init! []
  (app-routes)
  (mount-root))

(defn log [x] (.log js/console x))
(-> (.get js/axios "https://jsonplaceholder.typicode.com/posts/1")
  (.then log))
