package com.svanekeapps.hvorerbat.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.svanekeapps.hvorerbat.R
import com.svanekeapps.hvorerbat.api.Bus
import com.svanekeapps.hvorerbat.databinding.MapFragmentBinding

class MapFragmentClicklistenerDelegate(
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

    private val buttonMap = mapOf(
        "1" to binding.fab1and4,
        "2" to binding.fab2,
        "3" to binding.fab3and5,
        "4" to binding.fab1and4,
        "5" to binding.fab3and5,
        "6" to binding.fab6,
        "7" to binding.fab7and8,
        "8" to binding.fab7and8,
        "9" to binding.fab9,
        "10" to binding.fab10,
        "21" to binding.fab21and23,
        "23" to binding.fab21and23

    )

    private fun colorGenerator(color: Int): ColorStateList {
        return ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }

    private val colorMap = mapOf(
        "1" to colorGenerator(R.color.Bus1and4),
        "2" to colorGenerator(R.color.Bus2),
        "3" to colorGenerator(R.color.Bus3and5),
        "4" to colorGenerator(R.color.Bus1and4),
        "5" to colorGenerator(R.color.Bus3and5),
        "6" to colorGenerator(R.color.Bus6),
        "7" to colorGenerator(R.color.Bus7and8),
        "8" to colorGenerator(R.color.Bus7and8),
        "9" to colorGenerator(R.color.Bus9),
        "10" to colorGenerator(R.color.Bus10),
        "21" to colorGenerator(R.color.standard),
        "23" to colorGenerator(R.color.standard)
    )

    private val resourceMap = mapOf(
        "1" to R.drawable.ic_bus_1_4,
        "2" to R.drawable.ic_bus_2,
        "3" to R.drawable.ic_bus_3_5,
        "4" to R.drawable.ic_bus_1_4,
        "5" to R.drawable.ic_bus_3_5,
        "6" to R.drawable.ic_bus_6,
        "7" to R.drawable.ic_bus_7_8,
        "8" to R.drawable.ic_bus_7_8,
        "9" to R.drawable.ic_bus_9,
        "10" to R.drawable.ic_bus_10,
        "21" to R.drawable.ic_bus_by,
        "23" to R.drawable.ic_bus_by
    )




    fun loadVisibilityMap(busList: List<Bus>): MutableMap<String, Boolean> {
        busList.forEach { bus ->
            if (markerVisibilityMap[bus.title] == null || markerVisibilityMap[bus.title] == false) {
                markerVisibilityMap[bus.title] = false
            }
        }

        buttonMap.forEach { button ->
            button.value.isClickable = false
            button.value.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyout))
            button.value.alpha = 0.5F

            // check if button is clicked, and make sure it can be unclicked in case a bus goes out of route
            if(button.value.tag != null){
                if(button.value.tag.toString() == R.drawable.ic_checked.toString()){
                    button.value.isClickable = true

                }
            }
        }

        buttonMap.forEach { button ->
            if (!busList.none { it.title == button.key }) {
                button.value.backgroundTintList = colorMap[button.key]
                button.value.isClickable = true
                button.value.alpha = 1F
            }else{
                markerVisibilityMap[button.key] = false
//                resourceMap[button.key]?.let { button.value.setImageResource(it) }
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

    // sets all markers to visible if no filters is clicked.
    // also sets polyline to visibile if no filters is clicked

    private fun markerResetCheck(polyLine: Polyline?, viewModel: BusViewModel, busInfoTrigger: Boolean) {
        if (!markerVisibilityMap.containsValue(true)) {
            allMarkers.forEach { marker ->
                marker.isVisible = true
            }
            polyLine?.isVisible = true
            if(busInfoTrigger){
                binding.busInfo.visibility = View.VISIBLE
            }else{
                binding.busInfo.visibility = View.INVISIBLE
            }

        }else{
            var visibility = false
            markerVisibilityMap.forEach { marker ->
                if(marker.value && polyLine?.color == viewModel.getColor(context, marker.key)){
                    visibility = true

                }

            }
            polyLine?.isVisible = visibility

            // making sure to hide and show busInfo correctly
            if(visibility && busInfoTrigger){
                binding.busInfo.visibility = View.VISIBLE
            }else{
                binding.busInfo.visibility = View.INVISIBLE
            }
        }
    }


    fun fab1and4ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab1and4IsOpen) {
            binding.fab1and4.setImageResource(R.drawable.ic_checked)
            binding.fab1and4.tag = R.drawable.ic_checked
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
            binding.fab1and4.tag = R.drawable.ic_bus_1_4
            markerVisibilityMap["1"] = false
            markerVisibilityMap["4"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "1" || marker.title == "4") {
                    marker.isVisible = false
                }
            }
            fab1and4IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }


    fun fab2ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab2IsOpen) {
            binding.fab2.setImageResource(R.drawable.ic_checked)
            binding.fab2.tag = (R.drawable.ic_checked)
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
            binding.fab2.tag = R.drawable.ic_bus_2
            markerVisibilityMap["2"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "2") {
                    marker.isVisible = false
                }
            }

            fab2IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }

    fun fab21and23ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab21and23IsOpen) {
            binding.fab21and23.setImageResource(R.drawable.ic_checked)
            binding.fab21and23.tag = R.drawable.ic_checked
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
            binding.fab21and23.tag = R.drawable.ic_bus_by
            markerVisibilityMap["21"] = false
            markerVisibilityMap["23"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "21" || marker.title == "23") {
                    marker.isVisible = false
                }
            }
            fab21and23IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }

    fun fab10ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab10IsOpen) {
            binding.fab10.setImageResource(R.drawable.ic_checked)
            binding.fab10.tag = R.drawable.ic_checked
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
            binding.fab10.tag = R.drawable.ic_bus_10
            markerVisibilityMap["10"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "10") {
                    marker.isVisible = false
                }
            }
            fab10IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }

    fun fab9ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab9IsOpen) {
            binding.fab9.setImageResource(R.drawable.ic_checked)
            binding.fab9.tag = R.drawable.ic_checked
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
            binding.fab9.tag = R.drawable.ic_bus_9
            markerVisibilityMap["9"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "9") {
                    marker.isVisible = false
                }
            }

            fab9IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }

    fun fab6ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab6IsOpen) {
            binding.fab6.setImageResource(R.drawable.ic_checked)
            binding.fab6.tag = R.drawable.ic_checked
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
            binding.fab6.tag = R.drawable.ic_bus_6
            markerVisibilityMap["6"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "6") {
                    marker.isVisible = false
                }
            }

            fab6IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }

    fun fab3and5ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab3and5IsOpen) {
            binding.fab3and5.setImageResource(R.drawable.ic_checked)
            binding.fab3and5.tag = R.drawable.ic_checked
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
            binding.fab3and5.tag = R.drawable.ic_bus_3_5
            markerVisibilityMap["3"] = false
            markerVisibilityMap["5"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "3" || marker.title == "5") {
                    marker.isVisible = false
                }
            }

            fab3and5IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }

    fun fab7and8ClickListener(
        polyLine: Polyline?,
        viewModel: BusViewModel,
        busInfoTrigger: Boolean
    ): MutableMap<String, Boolean> {
        if (fab7and8IsOpen) {
            binding.fab7and8.setImageResource(R.drawable.ic_checked)
            binding.fab7and8.tag = R.drawable.ic_checked
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
            binding.fab7and8.tag = R.drawable.ic_bus_7_8
            markerVisibilityMap["7"] = false
            markerVisibilityMap["8"] = false
            allMarkers.forEach { marker ->
                if (marker.title == "7" || marker.title == "8") {
                    marker.isVisible = false
                }
            }

            fab7and8IsOpen = true
        }
        markerResetCheck(polyLine, viewModel, busInfoTrigger)
        return markerVisibilityMap
    }
}