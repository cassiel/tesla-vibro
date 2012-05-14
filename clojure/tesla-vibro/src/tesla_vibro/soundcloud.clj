;; SoundCloud interfacing.

(ns tesla-vibro.soundcloud
  (:require (clojure-soundcloud [core :as sc])
			(clojure.java [shell :as sh])
			(tesla-vibro [manifest :as m])))

(defprotocol UPLOADER
  (upload [this idx]))

(defn uploaderXXX [cfg sc-cfg]
  (let [sc-uploader (sc/soundcloud-handler sc-cfg)]
	(reify UPLOADER
	  (upload [this idx]
		(sh/sh "lame"
			   (m/full-audio-filename cfg idx)
			   (m/full-mp3-filename cfg idx))
		(sc/upload-file sc-uploader (m/full-mp3-filename cfg idx))))))

(defn uploader [cfg sc-cfg]
  (let [sc-uploader (sc/soundcloud-handler sc-cfg)]
	(reify UPLOADER
	  (upload [this idx]
		(sc/upload-file sc-uploader (m/full-audio-filename cfg idx))))))
