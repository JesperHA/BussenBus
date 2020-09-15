package com.holmapps.bussenbus.api

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import timber.log.Timber
import java.lang.reflect.Type

class RouteDeserializer() : JsonDeserializer<RouteResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): RouteResponse {
        val coordList: MutableList<Coordinate> = mutableListOf()
        val stopList: MutableList<BusStop> = mutableListOf()
        val response: RouteResponse = RouteResponse(coordList, stopList)

        json?.let {
            if (it.isJsonArray) {
                val jsonArray = it.asJsonArray
                if (jsonArray.size() > 0 && jsonArray[0].isJsonArray) {
                    addItems(jsonArray.asJsonArray, coordList, stopList)
                    Timber.i(coordList.toString())
                    Timber.i(stopList.toString())
                }
            }
        }
        return response
    }

    private fun addItems(jsonArray: JsonArray, coordList: MutableList<Coordinate>, stopList: MutableList<BusStop>) {
        val coordJson = jsonArray[0].asJsonArray
        val stopJson = jsonArray[1].asJsonArray
        for (i in 0..coordJson.size()-1){
            val item = coordJson[i].asJsonArray
            val longtitude = item[0].asDouble / 1000000
            val latitude = item[1].asDouble / 1000000
            coordList.add(Coordinate(longtitude,latitude))
        }

        for (i in 0..stopJson.size()-1){
            val item = stopJson[i].asJsonArray
            val coordinate = coordList[item[0].asInt]
            val title = item[1].asString.replace(" (Bornholm)", "")
            stopList.add(BusStop(coordinate,title))
        }
    }

}
