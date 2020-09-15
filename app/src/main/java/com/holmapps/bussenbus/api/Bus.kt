package com.holmapps.bussenbus.api

import com.google.android.gms.maps.model.LatLng

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
    val coordinate: LatLng,
    val title:String
)