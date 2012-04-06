'''
Simple testing model: track Weavrs by location. This is
little more than an elementary normalised schema at this stage;
more to follow.
'''

import logging
from django.db import models
from webapp.models import AccessToken

class Weavr(models.Model):
    name = models.CharField(max_length=255, blank=False)
    token = models.ForeignKey(AccessToken)

    def __unicode__(self):
        return self.name

class Location(models.Model):
    weavr = models.ForeignKey(Weavr)
    created = models.DateTimeField(auto_now=True, auto_now_add=True, null=False)
    location = models.CharField(max_length=255, blank=False)

# Calls from view:
def listWeavrs():
    return Weavr.objects.all()

def getWeavr(id):
    return Weavr.objects.get(id=id)

def getLastLocation(weavrID):
    w = getWeavr(weavrID)
    locs = Location.objects.filter(weavr=weavrID)
    if len(locs) >= 1:
        return locs[0]
    else:
        return None
