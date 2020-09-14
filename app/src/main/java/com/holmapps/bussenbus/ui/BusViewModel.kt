package com.holmapps.bussenbus.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holmapps.bussenbus.api.Bus
import com.holmapps.bussenbus.repository.BusRepository
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class BusViewModel @Inject constructor(private val repository: BusRepository): ViewModel() {

    val liveBus: MutableLiveData<List<Bus>>

    init {
        liveBus = repository.getBusses()

//        loop()

        fetchBusLocations()
    }


    private fun loop(){
        viewModelScope.launch(Dispatchers.IO){
            delay(5000)
            fetchBusLocations()
            loop()
        }
    }

    fun fetchBusLocations() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBusLocations()
            Timber.i("Locations Fetched")
        }
    }

}