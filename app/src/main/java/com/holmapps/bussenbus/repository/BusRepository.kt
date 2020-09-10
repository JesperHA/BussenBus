package com.holmapps.bussenbus.repository

import com.holmapps.bussenbus.api.BusApi
import com.holmapps.bussenbus.api.BusResponse
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class BusRepository @Inject constructor(
    val api: BusApi,
    @Named("timeFormatter") private val timeFormatter: SimpleDateFormat
) {

    suspend fun fetchBusLocations(): Response<Response<BusResponse>> {

        val apiResponse = Response.success(api.getBusLocations("05:05:05"))
        if (apiResponse.isSuccessful) {
            Timber.i(apiResponse.toString())
        }
        return apiResponse
    }

    private fun formatTime(currentTime: String): String {
        return timeFormatter.format(Date(currentTime))
    }

}