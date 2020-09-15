package com.holmapps.bussenbus.api

import com.google.android.gms.maps.model.LatLng

data class BusResponse(val busList: List<Bus>)

data class RouteResponse(val routeList: List<LatLng>, val stopsList: List<BusStop>)