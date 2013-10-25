(ns svobodni.handler
  (:use compojure.core hiccup.page hiccup.element)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [saxon :as xml]))

(defroutes app-routes
  (GET "/" [] 
       (let [xdoc (xml/compile-xml (java.net.URL. "http://www.volby.cz/pls/ps2013/vysledky"))
             data (map #(.getValue (.getTypedValue %)) 
                        (xml/query "/v:VYSLEDKY/v:CR/v:STRANA[@KSTRANA=2]/v:HODNOTY_STRANA/@*" 
                                   {:v "http://www.volby.cz/ps/"} xdoc))]
         (html5 [:head 
                 [:title "Jsou Svobodní v parlamentu?"]
                 (include-js "//use.edgefonts.net/asap.js")
                 (include-css "/style.css")]
                [:body
                 (image "/logo.jpg")
                 [:h1 "Jsou Svobodní v parlamentu?"]
                 [:h2 (str (last data) " %")]
                 [:p (str "Celkový počet hlasů: " (first data))]
                 [:p {:class "footer"} 
                    "Horkou jehlou ušil "
                    (link-to "https://twitter.com/dkvasnickajr" "@dkvasnickajr")
                    " v programovacím jazyce Clojure | Používá OpenData API ze serveru Volby.cz"]])))
           
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
