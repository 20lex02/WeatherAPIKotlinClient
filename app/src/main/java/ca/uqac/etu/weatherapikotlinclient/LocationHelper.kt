package ca.uqac.etu.weatherapikotlinclient

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

class LocationHelper(private val locationManager : LocationManager) {
    fun getLocation(activity: Activity) : Location? {
        val locationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(getProvider()!!)
        }
        return null
    }

    private fun getProvider(): String? {
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isCostAllowed = false
        criteria.powerRequirement = Criteria.POWER_MEDIUM
        return locationManager.getBestProvider(criteria, true)
    }
}
