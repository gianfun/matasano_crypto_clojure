(defproject matasano_crypto "0.1.0-SNAPSHOT"
  :description "My take at solving the Matasano Crypto Challenge in Clojure"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :plugins [[lein-midje "3.2.1"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [midje "1.9.0"]]
  :repl-options {:init-ns matasano-crypto.core})
