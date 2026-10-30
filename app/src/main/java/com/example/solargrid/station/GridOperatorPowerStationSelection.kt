package com.example.solargrid.station

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.solargrid.R
import com.example.solargrid.dashboard.GridOperatorDashboard
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

class GridOperatorPowerStationSelection : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager

    private var map: MapLibreMap? = null
    private var loadedStyle: Style? = null
    private var currentLocation: Location? = null

    private var locationEnabled = false
    private var firstFixHandled = false

    private data class PowerStation(
        val name: String,
        val latitudeOffset: Double,
        val longitudeOffset: Double
    )

    private val dummyStations = listOf(
        PowerStation("Grid Substation A", 0.003, 0.003),
        PowerStation("Grid Substation B", -0.004, 0.002),
        PowerStation("Grid Substation C", 0.002, -0.005),
        PowerStation("Grid Substation D", -0.006, -0.003),
        PowerStation("Grid Substation E", 0.007, 0.001)
    )

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onLocationUpdate(location)
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (hasLocationPermission()) {
                enableLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission is needed to find nearby stations.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT

        MapLibre.getInstance(this)
        setContentView(R.layout.activity_grid_operator_power_station_selection)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val header = findViewById<View>(R.id.dashboardHeader)
            header?.setPadding(
                header.paddingLeft,
                systemBars.top + (12 * resources.displayMetrics.density).toInt(),
                header.paddingRight,
                header.paddingBottom
            )
            insets
        }

        mapView = findViewById(R.id.mapView)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        mapView.onCreate(savedInstanceState)

        // Back button -> Navigates back to Grid Operator Dashboard
        findViewById<ImageView>(R.id.powerBackButton).setOnClickListener {
            val intent = Intent(this, GridOperatorDashboard::class.java)
            startActivity(intent)
            finish()
        }

        mapView.getMapAsync { mapLibreMap ->
            map = mapLibreMap

            findViewById<TextView>(R.id.zoomInButton).setOnClickListener {
                mapLibreMap.animateCamera(CameraUpdateFactory.zoomIn())
            }

            findViewById<TextView>(R.id.zoomOutButton).setOnClickListener {
                mapLibreMap.animateCamera(CameraUpdateFactory.zoomOut())
            }

            mapLibreMap.setOnMarkerClickListener { marker ->
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.dialog_grid_operator_power_station)

                val stationTitle = dialog.findViewById<TextView>(R.id.stationTitle)
                val closeBtn = dialog.findViewById<View>(R.id.closeButton)

                stationTitle.text = marker.title

                closeBtn?.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()

                dialog.window?.setLayout(
                    (300 * resources.displayMetrics.density).toInt(),
                    WindowManager.LayoutParams.WRAP_CONTENT
                )

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.window?.setGravity(Gravity.CENTER)

                true
            }

            mapLibreMap.setStyle(Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")) { style ->
                loadedStyle = style
                checkLocationPermissionAndEnable()
            }
        }
    }

    private fun checkLocationPermissionAndEnable() {
        if (hasLocationPermission()) {
            enableLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

    private fun enableLocation() {
        if (locationEnabled) return
        if (!hasLocationPermission()) return

        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    5f,
                    locationListener,
                    Looper.getMainLooper()
                )
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    2000L,
                    5f,
                    locationListener,
                    Looper.getMainLooper()
                )
            }

            val lastGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val lastNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val bestLast = lastGps ?: lastNet

            if (bestLast != null) {
                onLocationUpdate(bestLast)
            }

            locationEnabled = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun onLocationUpdate(location: Location) {
        currentLocation = location

        if (!firstFixHandled) {
            firstFixHandled = true

            val userLatLng = LatLng(location.latitude, location.longitude)

            map?.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(userLatLng)
                        .zoom(14.0)
                        .build()
                )
            )

            dummyStations.forEach { station ->
                val stationPos = LatLng(
                    location.latitude + station.latitudeOffset,
                    location.longitude + station.longitudeOffset
                )

                map?.addMarker(
                    MarkerOptions()
                        .position(stationPos)
                        .title(station.name)
                        .snippet("Tap for station status")
                )
            }
        }
    }

    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() {
        super.onStop()
        mapView.onStop()
        if (locationEnabled) {
            try { locationManager.removeUpdates(locationListener) } catch (_: SecurityException) {}
            locationEnabled = false
        }
    }
    override fun onSaveInstanceState(outState: Bundle) { super.onSaveInstanceState(outState); mapView.onSaveInstanceState(outState) }
    override fun onLowMemory() { super.onLowMemory(); mapView.onLowMemory() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
}
