"trying to boot up".postln;

if ('DTMFGen'.asClass.notNil) {
	d = 'DTMFGen'.asClass.new
} {
	("cannot find DTMFGen").postln;
}
