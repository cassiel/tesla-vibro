// Instantiate a DTMF generating object. It does its own OSC messaging.
// (We should abstract them, but the code is so simple it hardly
// justifies the effort.) This should plug fairly neatly into the
// existing Philter Phactory SC setup (which seems to just be a script
// which dynamically loads handler instances for the sound generating
// classes.

"trying to boot up".postln;

if (\DTMFGen.asClass.notNil) {
	d = \DTMFGen.asClass.new;
} {
	("cannot find DTMFGen").postln;
}
