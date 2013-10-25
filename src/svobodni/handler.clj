(ns svobodni.handler
  (:use compojure.core hiccup.page hiccup.element)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [saxon :as xml]))

(defn attr-val [a] (-> a .getTypedValue .getValue))

(defn xpath [query xdoc] (xml/query (xml/with-default-ns "http://www.volby.cz/ps/" query) xdoc))

(defroutes app-routes
  (GET "/" [] 
       (let [xdoc (xml/compile-xml (java.net.URL. "http://www.volby.cz/pls/ps2013/vysledky"))
             totals (xml/compile-xml (xpath "/VYSLEDKY/CR" xdoc))
             data (map attr-val (xpath "/CR/STRANA[@KSTRANA=2]/HODNOTY_STRANA/@*" totals))             
             progress (attr-val (first (xpath "/CR/UCAST/@OKRSKY_ZPRAC_PROC" totals)))]
         (html5 [:head 
                 [:title "Jsou Svobodní v parlamentu?"]
                 (include-js "//use.edgefonts.net/asap.js")
                 (include-css "/style.css")]
                [:body
                 (image "/logo.jpg")
                 [:h2 "Jsou Svobodní v parlamentu?"]
                 [:h1 (last data) " %"]
                 [:p "Je sečteno " progress " % volebních okrsků | Celkový počet hlasů: " (first data)]
                 [:p {:class "footer"} 
                    "Horkou jehlou ušil "
                    (link-to "https://twitter.com/dkvasnickajr" "@dkvasnickajr")
                    " v programovacím jazyce Clojure | Používá OpenData API z Volby.cz | Zdrojáky jsou "
                    (link-to "https://github.com/dkvasnicka/svobodni" "na GitHubu")]])))
           
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
