package com.svanekeapps.hvorerbat.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.svanekeapps.hvorerbat.R
import com.svanekeapps.hvorerbat.api.Bus
import com.svanekeapps.hvorerbat.repository.BusRepository
import com.svanekeapps.hvorerbat.repository.RouteObject
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class BusViewModel @Inject constructor(private val repository: BusRepository) : ViewModel() {

    val liveBus: MutableLiveData<List<Bus>> = repository.getBusses()
    val routeCoordinates: MutableLiveData<RouteObject> = repository.getCoordinates()


    init {
        fetchBusLocations()
    }

    fun fetchBusRoute(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBusRoute(id)
            Timber.i("Routes Fetched")
        }
    }

    fun fetchBusLocations() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBusLocations()
            Timber.i("Locations Fetched")
        }
    }

    fun getMarkerIcon(context: Context, bus: Bus, zoomLevel: Float): BitmapDescriptor? {
        val markerView =
            CustomMarkerView(
                context,
                bus,
                zoomLevel,
                ::getColor
            )
        return BitmapDescriptorFactory.fromBitmap(getBitmapFromView(markerView))
    }

    fun getBitmapFromView(view: View): Bitmap? {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        var bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun getColor(context: Context, id: String): Int {

        return when (id) {
            "1" -> ContextCompat.getColor(context, R.color.Bus1and4)
            "2" -> ContextCompat.getColor(context, R.color.Bus2)
            "3" -> ContextCompat.getColor(context, R.color.Bus3and5)
            "4" -> ContextCompat.getColor(context, R.color.Bus1and4)
            "5" -> ContextCompat.getColor(context, R.color.Bus3and5)
            "6" -> ContextCompat.getColor(context, R.color.Bus6)
            "7" -> ContextCompat.getColor(context, R.color.Bus7and8)
            "8" -> ContextCompat.getColor(context, R.color.Bus7and8)
            "9" -> ContextCompat.getColor(context, R.color.Bus9)
            "10" -> ContextCompat.getColor(context, R.color.Bus10)
            "Færge" -> ContextCompat.getColor(context, R.color.Boat)
            else -> ContextCompat.getColor(context, R.color.standard)
        }
    }

    fun busInfoGenerator(busList: List<Bus>, tag: String): String {
        val builder = StringBuilder()
        var result = ""
        if (!busList.isEmpty()) {
            busList.forEach { bus ->
                if (bus.id == tag) {
                    builder.append("Bus ").append(bus.title).append(" mod ").append(bus.destination)
                        .append("\nNæste stop: ").append(bus.nextStop)
                        .append(
                            if (bus.delayInMins == "0" || bus.delayInMins.isEmpty()) {
                                "\nKører til tiden"
                            } else {
                                "\nForsinkelse: "
                            }
                        ).append(
                            if (bus.delayInMins == "0" || bus.delayInMins.isEmpty()) {
                                ""
                            } else {
                                bus.delayInMins
                            }
                        ).append(
                            if (bus.delayInMins == "0" || bus.delayInMins.isEmpty()) {
                                ""
                            } else if (bus.delayInMins == "1") {
                                " Minut"
                            } else {
                                " Minutter"
                            }

                        )
                    result = builder.toString()
                }
            }
        }
        return result
    }

}
