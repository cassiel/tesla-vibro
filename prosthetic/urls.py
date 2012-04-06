from django.conf.urls.defaults import *

import views

urlpatterns = patterns(
    '',
    (r"^list-weavrs/$", views.list_weavrs),
    (r"^get-location/(\d+)/$", views.get_location)
)
