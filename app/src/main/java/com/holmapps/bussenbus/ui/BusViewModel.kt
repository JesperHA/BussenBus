package com.holmapps.bussenbus.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holmapps.bussenbus.api.Bus
import com.holmapps.bussenbus.repository.BusRepository
import com.holmapps.bussenbus.repository.RouteObject
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class BusViewModel @Inject constructor(private val repository: BusRepository) : ViewModel() {

    val liveBus: MutableLiveData<List<Bus>>
    val routeCoordinates: MutableLiveData<RouteObject>


    init {
        liveBus = repository.getBusses()
        routeCoordinates = repository.getCoordinates()

//        loop()


        fetchBusLocations()

    }

    var job : Job? = null

    fun loop() {
        Timber.i("loopBool in loop function: " + loopBool.toString())
        job?.cancel()
        if (loopBool == true) {
            job = viewModelScope.launch(Dispatchers.IO) {
//                delay(100)
                repository.fetchBusLocations()
                delay(1000)
                loop()

            }
        }
    }

    fun setLoopBool(bool: Boolean) {
        loopBool = bool
        if(bool) {
            loop()
        }
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

}

var loopBool: Boolean = true