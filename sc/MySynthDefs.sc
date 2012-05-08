MySynthDefs {
        *generateSynthDefs {
                SynthDef(\beeple,
                         { arg freq = 440;
                           Out.ar(0, SinOsc.ar(freq, 0, 0.2)
                                     * Line.kr(1, 0, 0.5, doneAction: 2)
                                 )
                         }
                        ).store;
        }
}
