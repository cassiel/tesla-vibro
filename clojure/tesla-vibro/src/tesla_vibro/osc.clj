(ns tesla-vibro.osc
  (:import (net.loadbang.osc.comms UDPReceiver)))

(defprotocol CLOSEABLE
  (close [this]))

(defn start-service
  "Start an OSC listener on port, calling f (with raw Message types).
   Return a CLOSEABLE, so that we can close the port (for (re)testing.)"
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
    (reify CLOSEABLE (close [this] (.close receiver)))))
