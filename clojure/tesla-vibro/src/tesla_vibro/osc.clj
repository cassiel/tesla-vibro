;; -*- tab-width: 4; -*-

(ns tesla-vibro.osc
  (:require (tesla-vibro [manifest :as m]))
  (:import (net.loadbang.osc.data Message)
		   (net.loadbang.osc.comms UDPTransmitter UDPReceiver)
		   (java.net InetAddress)))

(defprotocol TRANSMITTER
  (transmit [this msg])
  (close-tr [this]))

(defprotocol RECEIVER
  (close-rc [this]))

(defn transmitter
  "Create a transmitter to localhost."
  [port]

  (let [tr (UDPTransmitter. (InetAddress/getByName "localhost") port)]
	(reify TRANSMITTER
	  (transmit [this msg]
		(.transmit tr msg))
	  (close-tr [this]
		(.close tr)))))

(defn start-receiver
  "Start an OSC listener on port, calling f (with raw Message types).
   Return a RECEIVER, so that we can close the port (for (re)testing.)"
  [port f]
  (let [receiver (proxy
					 [UDPReceiver]
					 [port]
				   (consumeMessage [ts msg] (f msg)))
		poller (fn []
				 (.take receiver)
				 (recur))
		runnable (proxy
					 [Runnable]
					 []
				   (run [] (poller)))]
	(.open receiver)
	(-> (Thread. runnable) .start)

	(reify RECEIVER (close-rc [this] (.close receiver)))))

(defn dtmf-start []
  (Message. "/dtmf/start"))

(defn dtmf-tone [idx]
  (-> (Message. "/dtmf/add-tone")
	  (.addInteger idx)))

(defn dtmf-render-to [cfg idx]
  (-> (Message. "/dtmf/render-to")
	  (.addString (m/full-audio-filename cfg idx))
	  (.addString (m/full-osc-filename cfg idx))
	  (.addInteger idx)))
