from django.http import HttpResponse
from models import Weavr, Location, listWeavrs, getWeavr, getLastLocation
from django.utils import simplejson as json
import logging

def list_weavrs(request):
    weavrs = listWeavrs()

    wlist = []
    for w in weavrs:
        wlist.append({"id" : w.id,
                      "token_id" : w.token.id,
                      "name" : w.name})
        
    return HttpResponse(json.dumps({"weavrs" : wlist}), mimetype='application/json')

def get_location(request, weavrID):
    loc = getLastLocation(weavrID)

    if loc is None:
        loc = ""
    else:
        loc = loc.location
    
    return HttpResponse(json.dumps({"location" : loc}))
