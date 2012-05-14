;; -*- tab-width: 4; -*-

(defproject tesla-vibro "1.0.0-SNAPSHOT"
  :description "Client for Weavrs Spine: talks to prosthetic and to SuperCollider server."
  :dependencies [[org.clojure/clojure "1.4.0"]
				 [cheshire "3.1.0"]
				 [http.async.client "0.4.3"]
				 [overtone/osc-clj "0.7.1"]
				 [net.loadbang/net.loadbang.osc "1.3.0"]
				 [eu.cassiel/clojure-soundcloud "0.2.0-SNAPSHOT"]]
  :dev-dependencies [[expectations "1.3.3"]])
