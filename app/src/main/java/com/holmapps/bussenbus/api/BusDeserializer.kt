package com.holmapps.bussenbus.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import timber.log.Timber
import java.lang.reflect.Type

class BusDeserializer(): JsonDeserializer<BusResponse> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BusResponse {
        val list: MutableList<Bus> = mutableListOf()
        val response: BusResponse = BusResponse(list)

        json?.let {
            if(it.isJsonArray){
                Timber.i(it.toString())
            }
        }


        return response
    }
}