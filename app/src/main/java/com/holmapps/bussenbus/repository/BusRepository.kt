package com.holmapps.bussenbus.repository

import androidx.lifecycle.MutableLiveData
import com.holmapps.bussenbus.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
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
    private var allBusses = mutableListOf<Bus>()

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
        allBusses.clear()
        allBusses.addAll(busList)
    }

    fun getBusses() = allBusses

    private fun formatTime(currentTime: Long): String {
        return timeFormatter.format(Date(currentTime))
    }

}