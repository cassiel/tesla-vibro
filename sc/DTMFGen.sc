DTMFGen {
	var itsClient;
	var itsScore;

	*new {
		^super.new.init;
	}

	init {
		itsScore = Score.new;
		this.addSynthDefs;
		this.initOSC;
		"DTMFGen initialised".postln;
		this.sayHello;
	}

	initOSC {
		itsClient = NetAddr("127.0.0.1", 57000);
		OSCresponder(nil, '/foo',
			{ |t, r, msg| ("INCOMING" + msg[1]).postln; }
		).add();
	}

	sayHello {
		itsClient.sendMsg('/hello', 1, "A", 5.8);
	}

	addSynthDefs {
		SynthDef(\beeple,
			{ arg freq = 440;
				Out.ar(0,   SinOsc.ar(freq, 0, 0.2)
						  * Line.kr(1, 0, 0.5, doneAction: 2)
				)
			}
		).store;
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
