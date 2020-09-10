package com.holmapps.bussenbus.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.holmapps.bussenbus.databinding.MapFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment() {

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

        viewModel.fetchBusLocations()

//        val fragmentManager = getActivity()?.supportFragmentManager
//            ?.findFragmentById(R.id.map) as? SupportMapFragment
//        fragmentManager?.getMapAsync(this)

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