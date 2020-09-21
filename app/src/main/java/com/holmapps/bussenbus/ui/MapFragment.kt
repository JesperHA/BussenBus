package com.holmapps.bussenbus.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {


    private val REQUEST_LOCATION: Int = 1
    private lateinit var mMap: GoogleMap
    private val uiScope = CoroutineScope(Main)
    private lateinit var polyLine: Polyline
    lateinit var busList: List<Bus>

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

    }

    // Putting markers in list to enable removal of markerrs without clearing entire map
    private val allMarkers = mutableListOf<Marker>()
    private val updateRateInMillis: Long = 1000


    private suspend fun plotBusMarkers(busList: List<Bus>, zoomLevel: Float) {

        allMarkers.forEach { marker ->
            marker.remove()
        }
        allMarkers.clear()

        if (currentBusId.isEmpty()){
            bus_info.visibility = View.INVISIBLE
            if (polylineExist) {
                polyLine.remove()
            }
        }

    if (busList.isNotEmpty()){
        for(i in 0 until busList[0].coordinatList.size() - 1) {

//            delay(500)



            busList.forEach { bus ->

                val array = bus.coordinatList[i].asJsonArray

                val longitude = array[0].asDouble / 1000000
                val latitude = array[1].asDouble / 1000000

                val markerOptions = MarkerOptions()

                markerOptions.position(LatLng(latitude, longitude))
                    .title(bus.title)
                    .icon(getMarkerIcon(bus, zoomLevel))
                    .anchor(0.5F, 0.5F)

                val newMarker: Marker = mMap.addMarker(markerOptions)

                newMarker.tag = bus.id
                allMarkers.add(newMarker)
            }

            delay(updateRateInMillis)
        }
        } else {
        bus_info.text = "Ingen busser kører på nuværende tidspunkt."
        bus_info.visibility = View.VISIBLE
        currentBusId = ""
    }
        if (currentBusId.isNotEmpty()) {
            bus_info.text = busInfoGenerator(currentBusId)
        }
        viewModel.fetchBusLocations()
    }



    private fun routeCoordinates(coordinateList: List<LatLng>) {

        if (polylineExist) {
            polyLine.remove()
        }
        polyLine = mMap.addPolyline(
            PolylineOptions().addAll(coordinateList).color(busColor)
        )
        polylineExist = true

    }

    private fun getMarkerIcon(bus: Bus, zoomLevel: Float): BitmapDescriptor? {
        val markerView = CustomMarkerView(requireContext(), bus, zoomLevel, ::getColor)
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

        mMap.setOnMapClickListener {
            bus_info.visibility = View.INVISIBLE
        }



        mMap.setOnMarkerClickListener {marker ->

            bus_info.visibility = View.VISIBLE
            currentBusId = marker.tag.toString()

            bus_info.text = busInfoGenerator(currentBusId)
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, zoomLevel))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, zoomLevel))
            busColor = getColor(marker.title)
            viewModel.fetchBusRoute(marker.tag.toString())
            true
        }


        viewModel.routeCoordinates.observe(viewLifecycleOwner, Observer {
            routeCoordinates(it.list)
        })
        var plotMarkerJob: Job? = null


        viewModel.liveBus.observe(viewLifecycleOwner, Observer {
            plotMarkerJob?.cancel()
            busList = it

            mMap.setOnMapLoadedCallback {
            plotMarkerJob = uiScope.launch(Main) {
                    plotBusMarkers(it, mMap.cameraPosition.zoom)
                }
            }
        })



        mMap.setOnCameraIdleListener {
            viewModel.fetchBusLocations()
        }

        var zoomCheck = mMap.cameraPosition.zoom
        mMap.setOnCameraMoveListener {
            plotMarkerJob?.cancel()
            if (zoomCheck != mMap.cameraPosition.zoom) {
                plotMarkerJob?.cancel()
                zoomCheck = mMap.cameraPosition.zoom
                zoomLevel = zoomCheck
            }
        }


        //moves compass button
        map_view.findViewWithTag<View>("GoogleMapMyLocationButton").parent?.let { parent ->
            val vg: ViewGroup = parent as ViewGroup
            vg.post {
                val mapCompass: View = parent.getChildAt(4)
                val rlp = RelativeLayout.LayoutParams(mapCompass.height, mapCompass.height)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)

                val topMargin = (58 * Resources.getSystem().displayMetrics.density).toInt()
                val rightMargin = (11 * Resources.getSystem().displayMetrics.density).toInt()
                rlp.setMargins(0, topMargin, rightMargin, 0)
                mapCompass.layoutParams = rlp
            }
        }
    }

    private fun busInfoGenerator(tag: String): String {
        val builder = StringBuilder()
        var result = ""
        if (!busList.isEmpty()) {
            busList.forEach { bus ->
                if (bus.id == tag) {
                    builder.append("Bus ").append(bus.title).append(" mod ").append(bus.destination)
                        .append("\nNæste stop: ").append(bus.nextStop)
                        .append(
                            if (bus.delayInMins == "0") {
                                "\nKører til tiden"
                            } else {
                                "\nForsinkelse: "
                            }
                        ).append(
                            if (bus.delayInMins == "0") {
                                ""
                            } else {
                                bus.delayInMins
                            }
                        )
                    result = builder.toString()
                }
            }
        }
        return result
    }

    private fun getColor(id: String): Int {

        return when (id) {
            "1" -> ContextCompat.getColor(requireContext(), R.color.Bus1and4)
            "2" -> ContextCompat.getColor(requireContext(), R.color.Bus2)
            "3" -> ContextCompat.getColor(requireContext(), R.color.Bus3and5)
            "4" -> ContextCompat.getColor(requireContext(), R.color.Bus1and4)
            "5" -> ContextCompat.getColor(requireContext(), R.color.Bus3and5)
            "6" -> ContextCompat.getColor(requireContext(), R.color.Bus6)
            "7" -> ContextCompat.getColor(requireContext(), R.color.Bus7and8)
            "8" -> ContextCompat.getColor(requireContext(), R.color.Bus7and8)
            "9" -> ContextCompat.getColor(requireContext(), R.color.Bus9)
            "10" -> ContextCompat.getColor(requireContext(), R.color.Bus10)
            "Færge" -> ContextCompat.getColor(requireContext(), R.color.Boat)
            else -> ContextCompat.getColor(requireContext(), R.color.standard)
        }
    }

    class CustomMarkerView(context: Context, bus: Bus, zoomLevel: Float, getColor: (id: String) -> Int) :
        ConstraintLayout(context) {

        init {
            LayoutInflater.from(context).inflate(R.layout.custom_bus_marker, this, true)

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

            if (maxFactor < zoomFactor) {
                lp.height = (lp.height * maxFactor).toInt()
                lp.width = (lp.width * maxFactor).toInt()
                title.textSize = (title.textSize * maxFactor)
            } else if (minFactor > zoomFactor) {
                lp.height = (lp.height * minFactor).toInt()
                lp.width = (lp.width * minFactor).toInt()
                title.textSize = (title.textSize * minFactor)
            } else {
                lp.height = height.toInt()
                lp.width = width.toInt()
                title.textSize = (title.textSize * zoomFactor).toFloat()
            }

            image.layoutParams = lp
            image.invalidate()

            title.text = bus.title
            image.setImageResource(bus.icon)
            delay.text = bus.delayInMins

            image.setColorFilter(getColor(bus.title))


            if (bus.delayInMins == "0" || TextUtils.isEmpty(bus.delayInMins)) {
                delay.visibility = GONE
            } else {
                delay.visibility = View.VISIBLE
                delay.text = bus.delayInMins
            }

        }
    }
}

private var zoomLevel = 0F
private var currentBusId = ""
private var polylineExist = false
private var busColor: Int = 1