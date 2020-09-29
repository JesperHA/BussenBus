package com.svanekeapps.hvorerbat.ui

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.svanekeapps.hvorerbat.api.Bus
import com.svanekeapps.hvorerbat.databinding.MapFragmentBinding
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
    private val updateRateInMillis: Long = 1000
    private val allMarkers = mutableListOf<Marker>()
    private var markerVisibilityMap = mutableMapOf<String, Boolean>()
    private lateinit var mapFragmentClicklistenerHelper: MapFragmentClicklistenerDelegate


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

        mapFragmentClicklistenerHelper = MapFragmentClicklistenerDelegate(binding, requireContext(), allMarkers)

        binding.fabMenu.setOnClickListener {mapFragmentClicklistenerHelper.fabMenuClickListener()}
        binding.fab1and4.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab1and4ClickListener() }
        binding.fab2.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab2ClickListener() }
        binding.fab3and5.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab3and5ClickListener() }
        binding.fab6.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab6ClickListener() }
        binding.fab7and8.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab7and8ClickListener() }
        binding.fab9.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab9ClickListener() }
        binding.fab10.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab10ClickListener() }
        binding.fab21and23.setOnClickListener { markerVisibilityMap = mapFragmentClicklistenerHelper.fab21and23ClickListener() }


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
        var zoomCheck = mMap.cameraPosition.zoom
        var plotMarkerJob: Job? = null

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))

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

        viewModel.routeCoordinates.observe(viewLifecycleOwner, Observer {
            plotRoute(it.list)
        })

        viewModel.liveBus.observe(viewLifecycleOwner, Observer {
            plotMarkerJob?.cancel()
            busList = it

            mMap.setOnMapLoadedCallback {
                plotMarkerJob = uiScope.launch(Main) {
                    plotBusMarkers(it, mMap.cameraPosition.zoom)
                }
            }
        })


        mMap.setOnMapClickListener {
            bus_info.visibility = View.INVISIBLE
            if(this::polyLine.isInitialized){
                polyLine.remove()
            }
        }

        mMap.setOnMarkerClickListener { marker ->

            bus_info.visibility = View.VISIBLE
            currentBusId = marker.tag.toString()

            bus_info.text = viewModel.busInfoGenerator(busList, currentBusId)

            cameraMove(marker.position)

            busColor = viewModel.getColor(requireContext(), marker.title)
            viewModel.fetchBusRoute(marker.tag.toString())
            true
        }

        mMap.setOnCameraIdleListener {
            viewModel.fetchBusLocations()
        }

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


    private fun cameraMove(position: LatLng) {
        if (zoomLevel < 10.5) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10.5F))
        } else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel))
        }
    }

    private fun plotRoute(coordinateList: List<LatLng>) {


        if (this::polyLine.isInitialized) {
                polyLine.remove()
        }
        polyLine = mMap.addPolyline(
            PolylineOptions().addAll(coordinateList).color(busColor)
        )
    }

    private suspend fun plotBusMarkers(
        busList: List<Bus>,
        zoomLevel: Float
    ) {

        if (busList.isEmpty()) {
            bus_info.visibility = View.INVISIBLE
            if (this::polyLine.isInitialized) {
                polyLine.remove()
            }
        }

        if (busList.isNotEmpty()) {
            for (i in 0 until busList[0].coordinatList.size() - 1) {
                allMarkers.forEach { marker ->
                    marker.remove()
                }
                allMarkers.clear()

                busList.forEach { bus ->
                    val array = bus.coordinatList[i].asJsonArray
                    val longitude = array[0].asDouble / 1000000
                    val latitude = array[1].asDouble / 1000000
                    val markerOptions = MarkerOptions()
//                    Timber.i("markerVisibilityMap: " + markerVisibilityMap.toString())
                    var visibility = true
                    if (markerVisibilityMap.containsValue(true)){

                        val markerVisibility = markerVisibilityMap[bus.title]
                        if (!markerVisibility!!) {
                            visibility = false
                        }
                    }
                    markerOptions.position(LatLng(latitude, longitude))
                        .title(bus.title)
                        .icon(viewModel.getMarkerIcon(requireContext(), bus, zoomLevel))
                        .anchor(0.5F, 0.5F)
                        .visible(visibility)


                    val newMarker: Marker = mMap.addMarker(markerOptions)

                    newMarker.tag = bus.id
                    allMarkers.add(newMarker)

                }
                markerVisibilityMap = mapFragmentClicklistenerHelper.loadVisibilityMap(busList)
                delay(updateRateInMillis)


            }
        } else {
            markerVisibilityMap = mapFragmentClicklistenerHelper.loadVisibilityMap(busList)
            bus_info.text = "Ingen busser kører på nuværende tidspunkt."
            bus_info.visibility = View.VISIBLE
            currentBusId = ""
        }

        if (currentBusId.isNotEmpty()) {
            if (bus_info != null) {
                    bus_info.text = viewModel.busInfoGenerator(busList, currentBusId)
                }
        }
        viewModel.fetchBusLocations()
    }


}

private var zoomLevel = 10.5F
private var currentBusId = ""
private var busColor: Int = 1