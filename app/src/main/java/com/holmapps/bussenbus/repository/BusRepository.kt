package com.holmapps.bussenbus.repository

import androidx.lifecycle.MutableLiveData
import com.holmapps.bussenbus.api.*
import okio.IOException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named


class BusRepository @Inject constructor(
    val api: BusApi,
    @Named("timeFormatter") private val timeFormatter: SimpleDateFormat
) {
    private var allBusses =  MutableLiveData<List<Bus>>()



    suspend fun fetchBusLocations(): ApiResponse<BusResponse> {
        return try {
            val apiResponse =
                ApiResponse.create(api.getBusLocations(formatTime(Calendar.getInstance(Locale.getDefault()).timeInMillis)))
            if (apiResponse is ApiSuccess) {
                saveBusses(apiResponse.body.busList)
            }
            Timber.i(apiResponse.toString())
            apiResponse
        } catch (e: IOException) {
            Timber.e(e)
            ApiResponse.create(e)
        }
    }

    private fun saveBusses(busList: List<Bus>){
        allBusses.postValue(busList)
    }

    fun getBusses() = allBusses

    private fun formatTime(currentTime: Long): String {
        return timeFormatter.format(Date(currentTime))
    }

}