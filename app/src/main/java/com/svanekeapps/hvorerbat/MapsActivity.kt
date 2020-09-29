package com.svanekeapps.hvorerbat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.svanekeapps.hvorerbat.ui.MapFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(){

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



        lateinit var mapFragment: MapFragment

        mapFragment = MapFragment.newInstance()
        supportFragmentManager
            .beginTransaction().replace(R.id.container, mapFragment)
            .commit()

//        BusDeserializer().deserialize( )

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val fragmentManager = supportFragmentManager
//            .findFragmentById(R.id.map) as? SupportMapFragment
//        fragmentManager?.getMapAsync(this)
    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//// Add a marker in Sydney and move the camera
//        val bornholm = LatLng(55.125436, 14.919486)
//        val zoom = 9.9F
//        mMap.addMarker(MarkerOptions().position(bornholm).title("Marker on Bornholm"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))
////    }
}