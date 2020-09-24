package com.svanekeapps.hvorerbat.ui

import android.content.Context
import android.view.animation.AnimationUtils
import com.google.android.gms.maps.model.Marker
import com.svanekeapps.hvorerbat.R
import com.svanekeapps.hvorerbat.databinding.MapFragmentBinding

class ClicklistenerHelperClass(bindingObject: MapFragmentBinding, context: Context, allMarkers: List<Marker>) {


    private val markerVisibilityMap = mutableMapOf<String, Boolean>()
    private val binding = bindingObject
//    private val markerVisibilityMap = markerVisibilityMapObject
    private val allMarkers = allMarkers

    private var isOpen = false
    private var fab1IsOpen = true
    private var fab2IsOpen = true
    private var fab3IsOpen = true
    private var fab4IsOpen = true
    private var fab5IsOpen = true
    private var fab6IsOpen = true
    private var fab7IsOpen = true
    private var fab8IsOpen = true


    val fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
    val fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)


    fun fabMenuClickListener() {
        if (isOpen) {
            binding.fab1.startAnimation(fabClose)
            binding.fab2.startAnimation(fabClose)
            binding.fab3.startAnimation(fabClose)
            binding.fab4.startAnimation(fabClose)
            binding.fab5.startAnimation(fabClose)
            binding.fab6.startAnimation(fabClose)
            binding.fab7.startAnimation(fabClose)
            binding.fab8.startAnimation(fabClose)
            isOpen = false
        } else {
            binding.fab1.startAnimation(fabOpen)
            binding.fab2.startAnimation(fabOpen)
            binding.fab3.startAnimation(fabOpen)
            binding.fab4.startAnimation(fabOpen)
            binding.fab5.startAnimation(fabOpen)
            binding.fab6.startAnimation(fabOpen)
            binding.fab7.startAnimation(fabOpen)
            binding.fab8.startAnimation(fabOpen)

            isOpen = true

        }
    }

    // Make map with bus.title as key, and marker.visible as true/false and make it toggle
    // when you click, thereafter check it in the plotmarkers function

    fun fab7ClickListener(): MutableMap<String, Boolean> {
        if (fab7IsOpen) {
            allMarkers.forEach { marker ->

                // whitelisting chosen markers in the markerVisibilityMap
                if (marker.title == "1" || marker.title == "4") {
                    marker.isVisible = true
                    markerVisibilityMap[marker.title] = true
                }

                // setting visibility to false on all other markers that is not on the whitelist
                if(marker.title != "1" && marker.title != "4"){
                    if (markerVisibilityMap[marker.title] == false || markerVisibilityMap[marker.title] == null){
                        marker.isVisible = false
                    }
                }

            }

            fab7IsOpen = false
//                Timber.i("MarkerVisibilityMap: " + markerVisibilityMap.toString())
        } else {
            allMarkers.forEach { marker ->
                if (marker.title == "1" || marker.title == "4") {
                    if (markerVisibilityMap["1"]!! || markerVisibilityMap["4"]!!){

//                        marker.isVisible = true
                        markerVisibilityMap[marker.title] = false

                    }


                }
                if(!markerVisibilityMap.containsValue(true)){
                    marker.isVisible = true
                }
            }
            fab7IsOpen = true
        }

        return markerVisibilityMap
    }

    fun fab6ClickListener(): MutableMap<String, Boolean> {
        if (fab6IsOpen) {
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    marker.isVisible = true
                    markerVisibilityMap[marker.title] = true
                }
            }

            fab6IsOpen = false
//                Timber.i("MarkerVisibilityMap: " + markerVisibilityMap.toString())
        } else {
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    marker.isVisible = false
                    markerVisibilityMap[marker.title] = false
                }

            }
            fab6IsOpen = true
        }
        return markerVisibilityMap
    }



}