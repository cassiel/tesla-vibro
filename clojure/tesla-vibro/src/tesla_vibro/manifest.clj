(ns tesla-vibro.manifest)

(defprotocol CONFIGURATION
  (full-audio-filename [this id])
  (full-osc-filename [this id])
  (full-mp3-filename [this id]))
