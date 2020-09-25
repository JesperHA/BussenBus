package com.svanekeapps.hvorerbat.ui

import android.content.Context
import android.view.animation.AnimationUtils
import com.google.android.gms.maps.model.Marker
import com.svanekeapps.hvorerbat.R
import com.svanekeapps.hvorerbat.api.Bus
import com.svanekeapps.hvorerbat.databinding.MapFragmentBinding

class ClicklistenerHelperClass(
    bindingObject: MapFragmentBinding,
    context: Context,
    allMarkersObject: List<Marker>
) {

    val context = context
    val markerVisibilityMap = mutableMapOf<String, Boolean>()
    private val binding = bindingObject
    private var allMarkers = allMarkersObject

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


    fun loadVisibilityMap(busList: List<Bus>): MutableMap<String, Boolean> {
        busList.forEach { bus ->
            if (markerVisibilityMap[bus.title] == null || markerVisibilityMap[bus.title] == false) {
                markerVisibilityMap[bus.title] = false
            }
        }
        return markerVisibilityMap
    }

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

    private fun markerResetCheck() {
        if (!markerVisibilityMap.containsValue(true)) {
            allMarkers.forEach { marker ->
                marker.isVisible = true
            }
        }
    }


    fun fab7ClickListener(): MutableMap<String, Boolean> {

        if (fab7IsOpen) {
            binding.fab7.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "1" || marker.title == "4") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab7IsOpen = false
        } else {
            binding.fab7.setImageResource(R.drawable.ic_bus_1_4)
            allMarkers.forEach { marker ->
                if (marker.title == "1" || marker.title == "4") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }

            }
            markerResetCheck()

            fab7IsOpen = true
        }

        return markerVisibilityMap
    }


    fun fab6ClickListener(): MutableMap<String, Boolean> {
        if (fab6IsOpen) {

            binding.fab6.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab6IsOpen = false
        } else {

            binding.fab6.setImageResource(R.drawable.ic_bus_2)
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab6IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab1ClickListener(): MutableMap<String, Boolean> {
        if (fab1IsOpen) {
            binding.fab1.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "21" || marker.title == "23") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab1IsOpen = false
        } else {
            binding.fab1.setImageResource(R.drawable.ic_bus_by)
            allMarkers.forEach { marker ->
                if (marker.title == "21" || marker.title == "23") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab1IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab2ClickListener(): MutableMap<String, Boolean> {
        if (fab2IsOpen) {
            binding.fab2.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "10") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab2IsOpen = false
        } else {
            binding.fab2.setImageResource(R.drawable.ic_bus_10)
            allMarkers.forEach { marker ->
                if (marker.title == "10") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab2IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab3ClickListener(): MutableMap<String, Boolean> {
        if (fab3IsOpen) {
            binding.fab3.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "9") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab3IsOpen = false
        } else {
            binding.fab3.setImageResource(R.drawable.ic_bus_9)
            allMarkers.forEach { marker ->
                if (marker.title == "9") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab3IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab4ClickListener(): MutableMap<String, Boolean> {
        if (fab4IsOpen) {
            binding.fab4.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "6") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab4IsOpen = false
        } else {
            binding.fab4.setImageResource(R.drawable.ic_bus_6)
            allMarkers.forEach { marker ->
                if (marker.title == "6") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab4IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab5ClickListener(): MutableMap<String, Boolean> {
        if (fab5IsOpen) {
            binding.fab5.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "3" || marker.title == "5") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab5IsOpen = false
        } else {
            binding.fab5.setImageResource(R.drawable.ic_bus_3_5)
            allMarkers.forEach { marker ->
                if (marker.title == "3" || marker.title == "5") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab5IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab8ClickListener(): MutableMap<String, Boolean> {
        if (fab8IsOpen) {
            binding.fab8.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "7" || marker.title == "8") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab8IsOpen = false
        } else {
            binding.fab8.setImageResource(R.drawable.ic_bus_7_8)
            allMarkers.forEach { marker ->
                if (marker.title == "7" || marker.title == "8") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab8IsOpen = true
        }
        return markerVisibilityMap
    }
}