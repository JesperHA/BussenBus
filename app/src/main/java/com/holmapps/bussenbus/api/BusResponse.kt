package com.holmapps.bussenbus.api

data class BusResponse(val busList: List<Bus>)

data class RouteResponse(val routeList: List<Coordinate>, val stopsList: List<BusStop>)