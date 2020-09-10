package com.holmapps.bussenbus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.holmapps.bussenbus.ui.MapFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        lateinit var mapFragment : MapFragment



        mapFragment = MapFragment.newInstance()
        supportFragmentManager
            .beginTransaction().replace(R.id.container, mapFragment)
            .commit()

//        BusDeserializer().deserialize( )

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val fragmentManager = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        fragmentManager.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

// Add a marker in Sydney and move the camera
        val bornholm = LatLng(55.125436, 14.919486)
        val zoom = 9.9F
mMap.addMarker(MarkerOptions().position(bornholm).title("Marker on Bornholm"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bornholm, zoom))
    }
}