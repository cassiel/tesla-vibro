(ns tesla-vibro.morse
  (:use overtone.live))

(def DOT-MS 100)
(def DASH-MS (* 3 DOT-MS))
(def TONE-GAP-MS DOT-MS)
(def CHAR-GAP-MS (* 3 DOT-MS))
(def WORD-GAP-MS (* 5 DOT-MS))

(def CODE {\A ". -"
           \B "- . . ."
           \C "- . - ."
           \D "- . ."
           \E "."
           \F ". . - ."
           \G "- - ."
           \H ". . . ."
           \I ". ."
           \J ". - - -"
           \K "- . -"
           \L ". - . ."
           \M "- -"
           \N "- ."
           \O "- - -"
           \P " . - - ."
           \Q "- - . -"
           \R ". - ."
           \S ". . ."
           \T "-"
           \U ". . -"
           \V ". . . -"
           \W ". - -"
           \X "- . . -"
           \Y "- . - -"
           \Z "- - . ."
           \0 "- - - - -"
           \1 ". - - - -"
           \2 ". . - - -"
           \3 ". . . - -"
           \4 ". . . . -"
           \5 ". . . . ."
           \6 "- . . . ."
           \7 "- - . . ."
           \8 "- - - . ."
           \9 "- - - - ."})

(defsynth beep
  "Rather ugly: creates a synth which beeps and then frees itself."
  [len DOT-MS]
  (out 0 (* (line 1.0 1.0 (/ len 1000.0) :action FREE)
            (sin-osc 440))))

(defn beep-char-bits
  "Beep a character as a vector of 0/1 values. We do an immediate
   reduce over the bits and schedule the beeps into the future.
   Returns the timestamp one tone-gap after the last beep."
  [start-t bits]
  (reduce (fn [t bit]
            (let [len ([DOT-MS DASH-MS] bit)]
              (at t (beep len))
              (+ t (+ len TONE-GAP-MS))))
          start-t
          bits))

(defn beep-char
  "Beep out a character (case-sensitive), return timestamp at the end of
   the last beep. Returns start time for character not found."
  [start-t ch]
  (if (contains? CODE ch)
    (let [bits (map {"." 0 "-" 1}
                    (clojure.string/split (CODE ch) #"\s+"))]
      (- (beep-char-bits start-t bits) TONE-GAP-MS))
    start-t))

(defn beep-chars
  "Beep out characters, putting out a word gap for spaces.
   Returns the time position at the end of the last beep,
   or beyond the space if it's the last character.
   [That's TODO: right now we return the end of the render.]"
  [start-t chars]
  (reduce (fn [t ch]
            (if (= ch \space)
              (+ t WORD-GAP-MS)
              (+ CHAR-GAP-MS (beep-char t ch))))
          start-t chars))
