DTMFGen {
        var itsClient;
        var itsScore;
        var itsOffset;						// Counter for time offset for tones.

        const theFreqs1 = #[697, 770, 852, 941];
        const theFreqs2 = #[1209, 1336, 1477, 1633];

        const thePositions = #[
                /* \1 */ [0, 0],
                /* \2 */ [0, 1],
                /* \3 */ [0, 2],
                /* \A */ [0, 3],
                /* \4 */ [1, 0],
                /* \5 */ [1, 1],
                /* \6 */ [1, 2],
                /* \B */ [1, 3],
                /* \7 */ [2, 0],
                /* \8 */ [2, 1],
                /* \9 */ [2, 2],
                /* \C */ [2, 3],
                /* \* */ [3, 0],
                /* \0 */ [3, 1],
                /* \# */ [3, 2],
                /* \D */ [3, 3]
        ];

        const toneSpaceMSec = 1000;
        const toneDurationMSec = 750;

        *new {
                ^super.new.init;
        }

        init {
                this.addSynthDefs;
                this.resetRecorder;
                this.initOSC;
                "DTMFGen initialised".postln;
        }

        initOSC {
                itsClient = NetAddr("127.0.0.1", 57000);

                OSCresponder(nil, '/start',
                        // Prime the recorder. (We're not multithreaded, obviously.)
                        { |t, r, msg|
                                                        "[/start] resetting recorder".postln;
                                                        this.resetRecorder;
                                                }
                ).add();

                OSCresponder(nil, '/add-dtmf',
                        // Add a DTMF tone: arg is index (0..15) - see thePositions.
                        { |t, r, msg|
                                                        "[/add-dtmf]".postln;
                                                        this.addTone(msg[1]);
                                                }
                ).add();

                // TODO: a method for adding an envelope section.

                OSCresponder(nil, '/render',
                        { |t, r, msg|
                                                        "[/render]".postln;
                                // Render out to file fully named as first arg. Reply when done.
                                this.renderTo(msg[1]);
                                itsClient.sendMsg('/rendered');
                        }
                ).add();
        }

        addSynthDefs {
                SynthDef(\beeple,
                        { |freq1, freq2|
                                Out.ar(0,   (SinOsc.ar(freq1, 0, 0.2) + SinOsc.ar(freq2, 0, 0.2))
                                        * Line.kr(1, 1, toneDurationMSec / 1000, doneAction: 2)
                                )
                        }
                ).store;
        }

        resetRecorder {
                "resetRecorder".postln;
                itsScore = Score.new;
                itsOffset = 0;
        }

        addTone {
                |idx|
                var freq1 = theFreqs1[thePositions[idx][0]];
                var freq2 = theFreqs2[thePositions[idx][1]];

                itsScore.add([itsOffset * (toneSpaceMSec / 1000),
                        [\s_new, \beeple, toneDurationMSec, 0, 0, \freq1, freq1, \freq2, freq2]]);
                // Not sure what all these args do!
                itsOffset = itsOffset + 1;
                ("addTone idx" + idx + "," + freq1 + "," + freq2).postln;
        }

        renderTo {
                |id|
                var filename = format("~/Desktop/output_%", id);
                var oscFilename = (filename ++ ".osc").standardizePath;
                var audioFilename = (filename ++ ".aiff").standardizePath;

                itsScore.recordNRT(
                        oscFilePath: oscFilename,
                        outputFilePath: audioFilename,
                        sampleRate: 44100,
                        sampleFormat: "int16",
                        duration: itsOffset * (toneSpaceMSec / 1000)
                );
        }

        testRecord {
                itsScore.add([0.1, [\s_new, \beeple, 1000, 0, 0, \freq, 440]]);

                itsScore.recordNRT(
                        oscFilePath: "~/Desktop/outputOSC".standardizePath,
                        outputFilePath: "~/Desktop/outputSoundFile.aiff".standardizePath,
                        sampleRate: 44100,
                        sampleFormat: "int16",
                        duration: 5,
                        completionString: "; rm " ++ ("~/Desktop/outputOSC".standardizePath)
                );
        }
}
