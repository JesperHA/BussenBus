package com.holmapps.bussenbus.api

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonArray

data class Bus(
    val title: String,
    var longtitude: Double,
    var latitude: Double,
    val coordinatList: JsonArray,
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