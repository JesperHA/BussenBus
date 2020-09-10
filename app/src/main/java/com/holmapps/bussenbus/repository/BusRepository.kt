package com.holmapps.bussenbus.repository

import androidx.lifecycle.LiveData
import com.holmapps.bussenbus.api.BusApi
import com.holmapps.bussenbus.api.BusResponse
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class BusRepository @Inject constructor(
    val api: BusApi,
    private val timeFormatter: SimpleDateFormat
) {

    fun fetchBusLocations(): Response<LiveData<Response<BusResponse>>> {



        val apiResponse = Response.success(api.getBusLocations(formatTime(Calendar.getInstance().time.date.toString())))
        if (apiResponse.isSuccessful) {
            Timber.i(apiResponse.toString())
        }

        return apiResponse

    }

    private fun formatTime(currentTime: String): String {
        return timeFormatter.format(Date(currentTime))
    }

}