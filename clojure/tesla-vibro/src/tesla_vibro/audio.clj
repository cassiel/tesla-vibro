(ns tesla-vibro.audio
  (:use overtone.live))

(defn generate [filestem]
  (let [buf (buffer 44100 1)
        syn (synth (record-buf (saw 440) buf :action FREE :loop 0))]
    (syn)
    (after-delay 1000 #(let [f (str "~/Desktop/" filestem ".wav")]
                         (do (println "SAVING" f)
                             (buffer-save buf f))))))

(defn generate' [buf]
  (let [buf-ugen (record-buf (saw 440) buf :action FREE :loop 0)
        syn (synth buf-ugen)]
    (syn)))
