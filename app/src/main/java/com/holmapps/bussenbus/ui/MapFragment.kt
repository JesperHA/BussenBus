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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


    private suspend fun plotBusMarkers(busList: List<Bus>, zoomFactor: Float) {
        Timber.i("Plotting busses")

//        allMarkers.forEach { marker ->
//            marker.remove()
//        }
//        allMarkers.clear()

        for(i in 0..4) {
            delay(500)
            allMarkers.forEach { marker ->
                marker.remove()
            }
            allMarkers.clear()

            busList.forEach { bus ->

                val array = bus.coordinatList[i].asJsonArray

                val longtitude = array[0].asDouble / 1000000
                val latitude = array[1].asDouble / 1000000

                val markerOptions = MarkerOptions()

                markerOptions.position(LatLng(latitude, longtitude))
                    .title(bus.title)
                    .icon(getMarkerIcon(bus, zoomFactor))
                    .anchor(0.5F, 0.5F)

                val marker: Marker = mMap.addMarker(markerOptions)

                marker.tag = bus.id
                allMarkers.add(marker)
            }
            Timber.i("plotLoop:")
            delay(500)
        }
        viewModel.fetchBusLocations()
        Timber.i("Plotted 5 times")
    }

    lateinit var polyLine: Polyline

    private fun routeCoordinates(coordinateList: List<LatLng>) {

        if (polylineExist == true) {
            polyLine.remove()
        }
        polyLine = mMap.addPolyline(
            PolylineOptions().addAll(coordinateList).color(busColor)
        )
        polylineExist = true

    }

    private fun getMarkerIcon(bus: Bus, zoomFactor: Float): BitmapDescriptor? {
        val markerView = CustomMarkerView(requireContext(), bus, zoomFactor)
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
        val bornholm = LatLng(55.125436, 14.919486)
        val zoom = 9.9F
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))
        mMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
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


        viewModel.routeCoordinates.observe(viewLifecycleOwner, Observer {
            routeCoordinates(it.list)
        })
        var job: Job? = null
        lateinit var busList: List<Bus>
        viewModel.liveBus.observe(viewLifecycleOwner, Observer {
            job?.cancel()
            val uiScope = CoroutineScope(Main)

            job = uiScope.launch(Main) {plotBusMarkers(it, mMap.cameraPosition.zoom)}
            busList = it
            isBusListInit = true

        })

        var zoomCheck = mMap.cameraPosition.zoom
        Timber.i("isBusListInit?: " + isBusListInit.toString())
        mMap.setOnCameraIdleListener() {
            viewModel.fetchBusLocations()
            viewModel.setLoopBool(true)

//            if (isBusListInit == true && zoomCheck != mMap.cameraPosition.zoom) {
////                plotBusMarkers(busList, mMap.cameraPosition.zoom)
//                viewModel.setLoopBool(false)
//                zoomCheck = mMap.cameraPosition.zoom
                Timber.i("Reaching OnCameraIdleListener")
//            }
        }

        mMap.setOnCameraMoveListener {
            job?.cancel()
            viewModel.setLoopBool(false)
            if (isBusListInit == true && zoomCheck != mMap.cameraPosition.zoom) {
                job?.cancel()
//                plotBusMarkers(busList, mMap.cameraPosition.zoom)
                viewModel.setLoopBool(false)
                zoomCheck = mMap.cameraPosition.zoom
                Timber.i("Reaching OnCameraIdleListener")
            }

        }
//        mMap.setOnCameraMoveStartedListener {
//            viewModel.setLoopBool(false)
//
//
//        }
    }

    private fun changeColor(id: String) {

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
            else -> busColor = resources.getColor(R.color.standard)
        }
    }

    class CustomMarkerView(context: Context, bus: Bus, zoomFactor: Float) :
        ConstraintLayout(context) {

        init {
            LayoutInflater.from(context).inflate(R.layout.custom_bus_marker, this, true)

            val image = findViewById<ImageView>(R.id.bus_circle)
            val title = findViewById<TextView>(R.id.bus_title)
            val delay = findViewById<TextView>(R.id.bus_delay)


            val maxSize = 210
            val minSize = 160
            val factor = zoomFactor / 12.5

            val lp =
                image.getLayoutParams()

            val height = lp.height * factor
            val width = lp.width * factor

            if (height > maxSize && width > maxSize) {
                lp.height = maxSize
                lp.width = maxSize
            } else if (height < minSize && width < minSize) {
                lp.height = minSize
                lp.width = minSize
            } else {
                lp.height = height.toInt()
                lp.width = width.toInt()
            }
            image.setLayoutParams(lp)
            image.invalidate()

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
                else -> image.setColorFilter(resources.getColor(R.color.standard))
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

private var isBusListInit = false
private var polylineExist = false
private var busColor: Int = 1