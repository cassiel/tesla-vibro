(ns tesla-vibro.audio
  (:use overtone.live))

(defn generate [filestem]
  (let [buf (buffer 44100 1)
        my-ugen (fn [] (saw 440))
        syn (synth (record-buf (my-ugen) buf :action FREE :loop 0))]
    (syn)
    (after-delay 1000 #(let [f (str "~/Desktop/" filestem ".wav")]
                         (do (println "SAVING" f)
                             (buffer-save buf f))))))

(defsynth beep [len 0.1]
  (out 0 (* (line 1.0 1.0 len :action FREE)
            (sin-osc 440))))

(def beep-lengths [0.1 0.2])

(def gap 0.15)

(defn do-beeps [bits]
  (reduce (fn [t bit]
            (let [len (beep-lengths bit)]
              (at t (beep len))
              (+ t (* 1000.0 (+ len gap)))))
          (now)
          bits))
