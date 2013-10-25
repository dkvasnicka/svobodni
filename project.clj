(defproject svobodni "0.1.0-SNAPSHOT"
  :url "http://svobodni-dk.rhcloud.com"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [clojure-saxon "0.9.3"]
                 [hiccup "1.0.4"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler svobodni.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
