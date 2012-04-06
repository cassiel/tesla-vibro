from django.http import HttpResponse
from models import Weavr, Location, listWeavrs
from django.utils import simplejson as json

def list_weavrs(request):
    weavrs = listWeavrs()

    wlist = []
    for w in weavrs:
        wlist.append({"id" : w.id,
                      "token_id" : w.token.id,
                      "name" : w.name})
        
    return HttpResponse(json.dumps({"weavrs" : wlist}), mimetype='application/json')
