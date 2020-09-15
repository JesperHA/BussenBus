package com.holmapps.bussenbus.api

data class Bus (
    val title: String,
    val longtitude: Double,
    val latitude: Double,
    val id: String,
    val nextStop: String,
    val icon: Int,
    val delayInMins: String
)

data class Coordinate (
    val longtitude: Double,
    val latitude: Double
)

data class BusStop (
    val coordinate: Coordinate,
    val title:String
)