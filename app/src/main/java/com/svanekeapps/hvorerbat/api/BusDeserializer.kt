package com.svanekeapps.hvorerbat.api

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.svanekeapps.hvorerbat.R
import timber.log.Timber
import java.lang.reflect.Type

class BusDeserializer() : JsonDeserializer<BusResponse> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BusResponse {
        val list: MutableList<Bus> = mutableListOf()
        val response: BusResponse = BusResponse(list)

        json?.let {
            if (it.isJsonArray) {
                val jsonArray = it.asJsonArray
                if (jsonArray.size() > 0 && jsonArray[0].isJsonArray) {
                    addBusItems(jsonArray[0].asJsonArray, list)
                    Timber.i(list.toString())
                }
            }
        }

        return response
    }

    private fun addBusItems(jsonArray: JsonArray, list: MutableList<Bus>) {

        for (i in 0 until jsonArray.size()) {
            val item = jsonArray[i].asJsonArray
            if (item.size() > 11 && item[0].isJsonPrimitive && item[1].isJsonPrimitive && item[2].isJsonPrimitive) {
                val title = item[0].asString.replace("Bus    ", "").trim()
                val longtitude = item[1].asDouble / 1000000
                val latitude = item[2].asDouble / 1000000
                val destination = item[7].asString.replace(" (Bornholm)", "")
                    .replace(" (Born", "")
                    .replace(" (B","")
                val coordinateList = item[8].asJsonArray
                val id = item[3].asString
                val nextStop = item[11].asString.replace(" (Bornholm)", "")
                    .replace(" (Born", "")
                    .replace(" (B","")
                var icon: Int
                if (title.startsWith("Færge")) {
                    icon = R.drawable.ic_baseline_directions_boat_24
                } else {
                    icon = R.drawable.ic_circle_24
                }
                val delayInMins = item[6].asString

                list.add(Bus(title, longtitude, latitude, coordinateList, id, destination, nextStop, icon, delayInMins))
            }

        }

    }
}