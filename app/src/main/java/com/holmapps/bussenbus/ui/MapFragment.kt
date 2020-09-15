package com.holmapps.bussenbus.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.holmapps.bussenbus.R
import com.holmapps.bussenbus.api.Bus
import com.holmapps.bussenbus.databinding.MapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.map_fragment.*
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {


    private val REQUEST_LOCATION: Int = 1
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

    val allMarkers = mutableListOf<Marker>()


    private fun plotBusMarkers(busList: List<Bus>) {

        allMarkers.forEach { marker ->
            marker.remove()

        }
        allMarkers.clear()

        busList.forEach { bus ->

            val markerOptions = MarkerOptions()

            markerOptions.position(LatLng(bus.latitude, bus.longtitude))
                .title(bus.title)
                .icon(getMarkerIcon(bus)).anchor(0.5F, 0.5F)

            val marker: Marker = mMap.addMarker(markerOptions)

            marker.tag = bus.id



            allMarkers.add(marker)
        }
        Timber.i("Markers: " + allMarkers.toString())
    }

    lateinit var polyLine: Polyline

    private fun routeCoordinates(id: String, coordinateList: List<LatLng>) {

        if(flag == true) {
            polyLine.remove()
        }
            polyLine = mMap.addPolyline(
            PolylineOptions().addAll(coordinateList).color(busColor)

        )

        flag = true

        Timber.i("PolyLines: " + polyLine.toString())
    }

    private fun getMarkerIcon(bus: Bus): BitmapDescriptor? {
        val markerView = CustomMarkerView(requireContext(), bus)
        return BitmapDescriptorFactory.fromBitmap(getBitmapFromView(markerView))
    }

    private fun getBitmapFromView(view: View): Bitmap? {

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        var bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
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
        mMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(requireActivity(), arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION)
        } else {
            mMap.isMyLocationEnabled = true
        }



        mMap.setOnMarkerClickListener {
            if (it.isInfoWindowShown) {
                it.hideInfoWindow()
            } else {
                it.showInfoWindow()
            }

            changeColor(it.title)

            viewModel.fetchBusRoute(it.tag.toString())

            true
        }
        val bornholm = LatLng(55.125436, 14.919486)
        val zoom = 9.9F
        //mMap.addMarker(MarkerOptions().position(bornholm).title("Marker on Bornholm"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))



        viewModel.routeCoordinates.observe(viewLifecycleOwner, Observer {
            routeCoordinates(it.id, it.list)
        })

        viewModel.liveBus.observe(viewLifecycleOwner, Observer {
            plotBusMarkers(it)
        })
    }

    private fun changeColor(id: String){

        when (id) {
            "1" -> busColor = resources.getColor(R.color.Bus1and4)
            "2" -> busColor = resources.getColor(R.color.Bus2)
            "3" -> busColor = resources.getColor(R.color.Bus3and5)
            "4" -> busColor = resources.getColor(R.color.Bus1and4)
            "5" -> busColor = resources.getColor(R.color.Bus3and5)
            "6" -> busColor = resources.getColor(R.color.Bus6)
            "7" -> busColor = resources.getColor(R.color.Bus7and8)
            "8" -> busColor = resources.getColor(R.color.Bus7and8)
            "9" -> busColor = resources.getColor(R.color.Bus9)
            "10" -> busColor = resources.getColor(R.color.Bus10)
            "Færge" -> busColor = resources.getColor(R.color.Boat)
        }

    }

    class CustomMarkerView(context: Context, bus: Bus) : ConstraintLayout(context) {

        init {
            LayoutInflater.from(context).inflate(R.layout.custom_bus_marker, this, true)

            val image = findViewById<ImageView>(R.id.bus_circle)
            val title = findViewById<TextView>(R.id.bus_title)
            val delay = findViewById<TextView>(R.id.bus_delay)

            title.text = bus.title
            image.setImageResource(bus.icon)
            delay.text = bus.delayInMins

            when (bus.title) {
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

            if (bus.delayInMins == "0" || TextUtils.isEmpty(bus.delayInMins)) {
                delay.visibility = GONE
            } else {
                delay.visibility = View.VISIBLE
                delay.text = bus.delayInMins
            }

        }
    }
}

private var flag = false
private var busColor: Int = 1