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
    private var fab21and23IsOpen = true
    private var fab10IsOpen = true
    private var fab9IsOpen = true
    private var fab6IsOpen = true
    private var fab3and5IsOpen = true
    private var fab2IsOpen = true
    private var fab1and4IsOpen = true
    private var fab7and8IsOpen = true

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
            binding.fab1and4.startAnimation(fabClose)
            binding.fab2.startAnimation(fabClose)
            binding.fab3and5.startAnimation(fabClose)
            binding.fab6.startAnimation(fabClose)
            binding.fab7and8.startAnimation(fabClose)
            binding.fab9.startAnimation(fabClose)
            binding.fab10.startAnimation(fabClose)
            binding.fab21and23.startAnimation(fabClose)
            isOpen = false
        } else {
            binding.fab1and4.startAnimation(fabOpen)
            binding.fab2.startAnimation(fabOpen)
            binding.fab3and5.startAnimation(fabOpen)
            binding.fab6.startAnimation(fabOpen)
            binding.fab7and8.startAnimation(fabOpen)
            binding.fab9.startAnimation(fabOpen)
            binding.fab10.startAnimation(fabOpen)
            binding.fab21and23.startAnimation(fabOpen)

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


    fun fab1and4ClickListener(): MutableMap<String, Boolean> {

        if (fab1and4IsOpen) {
            binding.fab1and4.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "1" || marker.title == "4") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab1and4IsOpen = false
        } else {
            binding.fab1and4.setImageResource(R.drawable.ic_bus_1_4)
            allMarkers.forEach { marker ->
                if (marker.title == "1" || marker.title == "4") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }

            }
            markerResetCheck()

            fab1and4IsOpen = true
        }

        return markerVisibilityMap
    }


    fun fab2ClickListener(): MutableMap<String, Boolean> {
        if (fab2IsOpen) {

            binding.fab2.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab2IsOpen = false
        } else {

            binding.fab2.setImageResource(R.drawable.ic_bus_2)
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab2IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab21and23ClickListener(): MutableMap<String, Boolean> {
        if (fab21and23IsOpen) {
            binding.fab21and23.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "21" || marker.title == "23") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab21and23IsOpen = false
        } else {
            binding.fab21and23.setImageResource(R.drawable.ic_bus_by)
            allMarkers.forEach { marker ->
                if (marker.title == "21" || marker.title == "23") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab21and23IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab10ClickListener(): MutableMap<String, Boolean> {
        if (fab10IsOpen) {
            binding.fab10.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "10") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab10IsOpen = false
        } else {
            binding.fab10.setImageResource(R.drawable.ic_bus_10)
            allMarkers.forEach { marker ->
                if (marker.title == "10") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab10IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab9ClickListener(): MutableMap<String, Boolean> {
        if (fab9IsOpen) {
            binding.fab9.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "9") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab9IsOpen = false
        } else {
            binding.fab9.setImageResource(R.drawable.ic_bus_9)
            allMarkers.forEach { marker ->
                if (marker.title == "9") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab9IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab6ClickListener(): MutableMap<String, Boolean> {
        if (fab6IsOpen) {
            binding.fab6.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "6") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab6IsOpen = false
        } else {
            binding.fab6.setImageResource(R.drawable.ic_bus_6)
            allMarkers.forEach { marker ->
                if (marker.title == "6") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab6IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab3and5ClickListener(): MutableMap<String, Boolean> {
        if (fab3and5IsOpen) {
            binding.fab3and5.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "3" || marker.title == "5") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab3and5IsOpen = false
        } else {
            binding.fab3and5.setImageResource(R.drawable.ic_bus_3_5)
            allMarkers.forEach { marker ->
                if (marker.title == "3" || marker.title == "5") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab3and5IsOpen = true
        }
        return markerVisibilityMap
    }

    fun fab7and8ClickListener(): MutableMap<String, Boolean> {
        if (fab7and8IsOpen) {
            binding.fab7and8.setImageResource(R.drawable.ic_checked)
            allMarkers.forEach { marker ->
                if (marker.title == "7" || marker.title == "8") {
                    markerVisibilityMap[marker.title] = true
                    marker.isVisible = true
                }
                if (markerVisibilityMap[marker.title] == false) {
                    marker.isVisible = false
                }
            }
            fab7and8IsOpen = false
        } else {
            binding.fab7and8.setImageResource(R.drawable.ic_bus_7_8)
            allMarkers.forEach { marker ->
                if (marker.title == "7" || marker.title == "8") {
                    markerVisibilityMap[marker.title] = false
                    marker.isVisible = false
                }
            }
            markerResetCheck()
            fab7and8IsOpen = true
        }
        return markerVisibilityMap
    }
}