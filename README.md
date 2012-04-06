# Tesla Vibro Prosthetic and Clojure Client

This is a simple [Weavrs][weavrs] prosthetic and associated
[Clojure][clojure] client for interrogating Weavrs. We've targetted
Clojure because that gets us bridged to [MaxMSP][max] (via our
[net.loadbang][nlclojure] package) or into [Overtone][overtone]. The
prosthetic is designed to run inside the Philter Phactory [prosthetic
runner][runner] (a [Django-nonrel][djnr] application) and hence in
[Google App Engine][gae]; symlink its `prosthetic` directory into the
runner with name `tesla` so that the URL's in the Clojure test code
work.

The prosthetic itself has a pretty simple data model: a table of
Weavrs which we've seen (and which are registered to use the
prosthetic) and a table of locations that the Weavrs visit (actually,
just the `street_address` field at the moment). A new location is
added if the polled location from a Weavr changes from the last one
seen.

There's a bit of Django MVC machinery which lets us interrogate the
data store: we can list all the Weavrs we've seen, and for any Weavr
we can fetch its last known location. Both calls return a JSON
structure. The Clojure code uses an asynchronous HTTP client and a
JSON parser to unpack the result.

[weavrs]: http://www.weavrs.com
[clojure]: http://clojure.org
[max]: http://cycling74.com/products/max/
[overtone]: http://overtone.github.com/
[nlclojure]: https://github.com/cassiel/net.loadbang.clojure
[runner]: https://github.com/philterphactory/prosthetic-runner
[djnr]: http://www.allbuttonspressed.com/projects/django-nonrel
[gae]: https://developers.google.com/appengine/
