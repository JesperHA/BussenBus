package com.holmapps.bussenbus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holmapps.bussenbus.repository.BusRepository
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class BusViewModel @Inject constructor(private val repository: BusRepository): ViewModel() {


    init {
        fetchBusLocations()
    }

    fun fetchBusLocations() {

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBusLocations()
            Timber.i("Locations Fetched")
        }
    }
}