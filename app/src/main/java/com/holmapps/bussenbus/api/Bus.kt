package com.holmapps.bussenbus.api

data class Bus (
    val title: String,
    val longtitude: Double,
    val latitude: Double,
    val nextStop: String,
    val icon: Int,
    val delayInMins: String
)