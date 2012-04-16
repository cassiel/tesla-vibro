from base_prosthetic import Prosthetic, persist_state
import logging
from tesla.models import Weavr, Location
from util import m_TokenStates

class TeslaKlang(Prosthetic):
    '''
    Basic prosthetic by Nick.
    '''
    # run throttle
    @classmethod
    def time_between_runs(cls):
        return 1

    # called from the task queue, once an hour (or whatever)
    @persist_state
    def act(self, force=False):
        '''
        A simple prosthetic method which appends a new location (street_address) for any
        Weavr which has moved since the last scan. We keep the last known location
        in the token state.
        '''
        weavrConfig = self.get("/1/weavr/configuration")
        weavrState = self.get("/1/weavr/state/")

        name = weavrConfig["name"]

        logging.info("Tesla key=%s name=%s" % (self.token.oauth_key, name))

        # I need to be convinced that get_or_create() provides any transactional
        # security in the GAE's datastore...
        w, created = Weavr.objects.get_or_create(token=self.token, name=name)

        if created:
            logging.info("First encounter for Weavr %s [%s]" % (name, self.token))
        else:
            logging.info("Already seen Weavr %s [%s]" % (name, self.token))

        # Get last known location, if any:
        if m_TokenStates.LAST_LOCATION in self.state:
            lastLocation = self.state[m_TokenStates.LAST_LOCATION]
        else:
            lastLocation = None

        # Get current location, if any:
        locations = self.get("/1/weavr/location/", params=dict(per_page=1))
        locs = locations[u'locations']
        if len(locs) == 1:
            currLocation = locs[0]['street_address']
        else:
            currLocation = None

        # Moved?
        #if currLocation not in [None, lastLocation, '']:
        if currLocation not in [None, '']: # FIXME: force move
            l = Location(weavr=w, location=currLocation)
            l.save()

            logging.info("MOVED Weavr id %s [%d], location now %s" % (name, w.id, currLocation))

            self.state[m_TokenStates.LAST_LOCATION] = currLocation
        else:
            logging.info("Weavr id %s [%d], still at %s" % (name, w.id, currLocation))

        return "OK"
