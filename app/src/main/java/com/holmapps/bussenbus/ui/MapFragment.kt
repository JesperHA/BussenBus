package com.holmapps.bussenbus.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.holmapps.bussenbus.api.Bus
import com.holmapps.bussenbus.databinding.MapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.map_fragment.*
import timber.log.Timber
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
            val busList = viewModel.busses
            busMarkers(busList)
            Timber.i(busList.toString())
}

//        val fragmentManager = getActivity()?.supportFragmentManager
//            ?.findFragmentById(R.id.map) as? SupportMapFragment
//        fragmentManager?.getMapAsync(this)

    }

    private fun busMarkers(busList: List<Bus>){
            mMap.clear()
        busList.forEach { bus ->
            val busPos = LatLng(bus.latitude, bus.longtitude)
            mMap.addMarker(MarkerOptions().position(busPos).title("Bus " + bus.title + " - " + bus.longtitude + ", " + bus.latitude))
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
    }

//    override fun onActivityCreated(p0: Bundle?) {
//        super.onActivityCreated(p0)
//
//        getMapAsync(this)
//    }



//    override fun onMapReady(googleMap: GoogleMap) {
//
//        mMap = googleMap
//        val bornholm = LatLng(55.125436, 14.919486)
//        val zoom = 9.9F
//        mMap.addMarker(MarkerOptions().position(bornholm).title("Marker on Bornholm"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))
//    }
}