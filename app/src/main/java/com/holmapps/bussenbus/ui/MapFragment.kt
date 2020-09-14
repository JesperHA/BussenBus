package com.holmapps.bussenbus.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.holmapps.bussenbus.R
import com.holmapps.bussenbus.api.Bus
import com.holmapps.bussenbus.databinding.MapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.map_fragment.*
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    companion object {
        fun newInstance() = MapFragment()
    }

    @Inject
    lateinit var viewModel: BusViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return MapFragmentBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = MapFragmentBinding.bind(view)


        binding.floatingActionButton.setOnClickListener {
            viewModel.fetchBusLocations()
}

    }

    private fun busMarkers(busList: List<Bus>){
            mMap.clear()
        busList.forEach { bus ->
            val busPos = LatLng(bus.latitude, bus.longtitude)
            mMap.addMarker(
                MarkerOptions().position(busPos)
                    .title("Bus " + bus.title + " - " + bus.longtitude + ", " + bus.latitude).icon(getMarkerIcon(bus)).anchor(0.5F, 0.5F)
            )
        }

    }

    private fun getMarkerIcon(bus: Bus): BitmapDescriptor? {
        val markerView = CustomMarkerView(requireContext(), bus)
        return BitmapDescriptorFactory.fromBitmap(getBitmapFromView(markerView))
    }

    private fun getBitmapFromView(view: View): Bitmap? {

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        var bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    class CustomMarkerView(context: Context, bus: Bus): ConstraintLayout(context){

        init {
            LayoutInflater.from(context).inflate(R.layout.custom_bus_marker, this, true)

            val image = findViewById<ImageView>(R.id.bus_circle)
            val title = findViewById<TextView>(R.id.bus_title)
            val delay = findViewById<TextView>(R.id.bus_delay)

            title.text = bus.title
            image.setImageResource(bus.icon)
            delay.text = bus.delayInMins

            when(bus.title){
                "1" -> image.setColorFilter(resources.getColor(R.color.Bus1and4))
                "2" -> image.setColorFilter(resources.getColor(R.color.Bus2))
                "3" -> image.setColorFilter(resources.getColor(R.color.Bus3and5))
                "4" -> image.setColorFilter(resources.getColor(R.color.Bus1and4))
                "5" -> image.setColorFilter(resources.getColor(R.color.Bus3and5))
                "6" -> image.setColorFilter(resources.getColor(R.color.Bus6))
                "7" -> image.setColorFilter(resources.getColor(R.color.Bus7and8))
                "8" -> image.setColorFilter(resources.getColor(R.color.Bus7and8))
                "9" -> image.setColorFilter(resources.getColor(R.color.Bus9))
                "10" -> image.setColorFilter(resources.getColor(R.color.Bus10))
                "Færge" -> image.setColorFilter(resources.getColor(R.color.Boat))
            }

            if(bus.delayInMins == "0" || TextUtils.isEmpty(bus.delayInMins)){
                delay.visibility = GONE
            } else {
                delay.visibility = View.VISIBLE
                delay.text = bus.delayInMins
            }

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)

    }

    //Based on this video: https://www.youtube.com/watch?v=m_H3JuybsJ0

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            mMap = it
        }
        val bornholm = LatLng(55.125436, 14.919486)
        val zoom = 9.9F
        //mMap.addMarker(MarkerOptions().position(bornholm).title("Marker on Bornholm"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))

        viewModel.liveBus.observe(viewLifecycleOwner, Observer {
            busMarkers(it)
        })
    }
}