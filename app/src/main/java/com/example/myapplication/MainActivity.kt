package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var locationManager = getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

              fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

              bt_location.setOnClickListener{
                  checkAndRequestLocationPermission()
                  getLocation()
              }

    }

    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) { ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun getLoc() {
        checkAndRequestLocationPermission()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses: List<Address> = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        ) as List<Address>
                            //afficher la latitude dans le textview
                            latitude.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude : </b><br></font>"
                                            + addresses[0].latitude)
                            )

                            // afficher la longitude dans le textview
                            longitude.setText(Html.fromHtml("<font color='#6200EE'><b>Longitude : </b><br></font>"
                                                    + addresses[0].longitude))
                            // afficher le pays dans le textview
                            pay.setText(Html.fromHtml("<font color='#6200EE'><b>Nom de pays : </b><br></font>"
                                                    + addresses[0].countryName))

                            // afficher la localité dans le textview
                            localite.setText(Html.fromHtml("<font color='#6200EE'><b>Localité : </b><br></font>"
                                                    + addresses[0].locality))

                            // afficher l’adresse dans le textview
                            adresse.setText(Html.fromHtml("<font color='#6200EE'><b>Adresse : </b><br></font>"
                                                    + addresses[0].getAddressLine(0)))

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Aucune position enregistrée", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
    }
}
