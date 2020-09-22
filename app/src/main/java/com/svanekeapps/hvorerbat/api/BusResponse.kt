package com.svanekeapps.hvorerbat.api

import com.google.android.gms.maps.model.LatLng

data class BusResponse(val busList: List<Bus>)

data class RouteResponse(val routeList: List<LatLng>, val stopsList: List<BusStop>)