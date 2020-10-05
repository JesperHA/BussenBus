package com.svanekeapps.hvorerbat.ui

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.svanekeapps.hvorerbat.R
import com.svanekeapps.hvorerbat.api.Bus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomMarkerView(
    context: Context,
    bus: Bus,
    zoomLevel: Float,
    getColor: (context: Context, id: String) -> Int
) :
    ConstraintLayout(context) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_bus_marker, this, true)

        val image = findViewById<ImageView>(R.id.bus_circle)
        val title = findViewById<TextView>(R.id.bus_title)
        val delay = findViewById<TextView>(R.id.bus_delay)

        val minFactor = 0.78F
        val maxFactor = 1.035F
        val zoomFactor = zoomLevel / 12.5
        title.textSize = 7F

        val lp = image.layoutParams

        val height = lp.height * zoomFactor
        val width = lp.width * zoomFactor

//        if (maxFactor < zoomFactor) {
//            lp.height = (lp.height * maxFactor).toInt()
//            lp.width = (lp.width * maxFactor).toInt()
//            title.textSize = (title.textSize * maxFactor)
//        } else if (minFactor > zoomFactor) {
//            lp.height = (lp.height * minFactor).toInt()
//            lp.width = (lp.width * minFactor).toInt()
//            title.textSize = (title.textSize * minFactor)
//        } else {
//            lp.height = height.toInt()
//            lp.width = width.toInt()
//            title.textSize = (title.textSize * zoomFactor).toFloat()
//        }

        lp.height = (lp.height * minFactor).toInt()
        lp.width = (lp.width * minFactor).toInt()
        title.textSize = (title.textSize * minFactor)

        image.layoutParams = lp
        image.invalidate()

        title.text = bus.title
        image.setImageResource(bus.icon)
        delay.text = bus.delayInMins

        image.setColorFilter(getColor(context, bus.title))


        if (bus.delayInMins == "0" || TextUtils.isEmpty(bus.delayInMins)) {
            delay.visibility = GONE
        } else {
            delay.visibility = View.VISIBLE
            delay.text = bus.delayInMins
        }

    }
}
