;; -*- tab-width: 4; -*-

;; --- Full render attempt.

(ns user
  (:require (tesla-vibro [manifest :as m]
						 [prosthetic :as p]
						 [osc :as osc]
						 [soundcloud :as our-sc])
			(clojure-soundcloud [core :as sc])
			(clojure.java [shell :as sh]))
  (:import (net.loadbang.osc.comms UDPTransmitter UDPReceiver)
		   (net.loadbang.osc.data Message)
		   (java.net InetAddress)))

(def cfg
  (let [stem "/Users/nick/Desktop/upload_%d.%s"]
	(reify m/CONFIGURATION
	  (full-audio-filename [this id]
		(format stem id "aiff"))
	  (full-osc-filename [this id]
		(format stem id "osc"))
	  (full-mp3-filename [this id]
		(format stem id "mp3")))))

(def upload-queue
  ^{:doc "Queue of files rendered from SuperCollider, awaiting upload."}
  (atom []))

(def t (osc/transmitter 57120))

(def r (osc/start-receiver
		57000
		(fn [msg]
		  (swap! upload-queue
				 #(conj % (-> msg (.getArgument 0) (.getValue)))))))

(deref upload-queue)

(osc/transmit t (osc/dtmf-start))

(doseq [i (range 16)]
  (osc/transmit t (osc/dtmf-tone i)))

(dotimes [i 16]
  (osc/transmit t (osc/dtmf-tone (- 15 i))))


(osc/transmit t (osc/dtmf-render-to cfg 419))
(osc/transmit t (osc/dtmf-render-to cfg 420))

(osc/close-tr t)
(osc/close-rc r)

(def sc-cfg (reify sc/CONFIGURATION
			  (sc-credentials-filename [this] "/Users/nick/Desktop/sc-credentials.json")))

(def u (our-sc/uploader cfg sc-cfg))

(our-sc/upload u 419)
(our-sc/upload u 420)







(def uploader (sc/soundcloud-handler sc-cfg))

(sc/upload-file uploader "/Users/nick/Desktop/garble.wav")

(sh/sh "ls", "-1")

(sh/sh "lame"
	   "/Users/nick/Desktop/garble.wav"
	   "/Users/nick/Desktop/garble.mp3")

(m/full-mp3-filename cfg 419)
