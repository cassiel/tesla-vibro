// Instantiate a DTMF generating object. It does its own OSC messaging.
// (We should abstract them, but the code is so simple it hardly
// justifies the effort.)

"trying to boot up".postln;

if ('DTMFGen'.asClass.notNil) {
	d = 'DTMFGen'.asClass.new
} {
	("cannot find DTMFGen").postln;
}
