package com.holmapps.bussenbus.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holmapps.bussenbus.api.Bus
import com.holmapps.bussenbus.repository.BusRepository
import com.holmapps.bussenbus.repository.RouteObject
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class BusViewModel @Inject constructor(private val repository: BusRepository): ViewModel() {

    val liveBus: MutableLiveData<List<Bus>>
    val routeCoordinates: MutableLiveData<RouteObject>

    init {
        liveBus = repository.getBusses()
        routeCoordinates = repository.getCoordinates()

//        loop()


        fetchBusLocations()

    }


    private fun loop(){
        viewModelScope.launch(Dispatchers.IO){
            delay(1000)
            fetchBusLocations()
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