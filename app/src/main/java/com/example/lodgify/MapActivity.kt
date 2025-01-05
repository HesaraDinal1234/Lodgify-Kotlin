package com.example.lodgify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var database: DatabaseReference

    companion object {
        private const val TAG = "MapActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("SelectedLocations")

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Enable user location if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }

        // Set a long click listener to select a location
        googleMap.setOnMapLongClickListener { latLng ->
            saveLocationToFirebase(latLng)
        }
    }

    private fun saveLocationToFirebase(latLng: LatLng) {
        // Add a marker at the selected location
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        // Save the location to Firebase
        val locationId = database.push().key ?: return
        val locationData = mapOf(
            "latitude" to latLng.latitude,
            "longitude" to latLng.longitude
        )

        database.child(locationId).setValue(locationData)
            .addOnSuccessListener {
                Toast.makeText(this, "Location saved successfully!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Location saved: $latLng")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to save location: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error saving location", exception)
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(this, "Location permission is required to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
