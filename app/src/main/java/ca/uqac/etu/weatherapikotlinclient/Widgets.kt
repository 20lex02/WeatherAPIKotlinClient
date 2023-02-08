package ca.uqac.etu.weatherapikotlinclient

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun WeatherWidget() {
    val forecast = remember { mutableStateOf(null as WeatherForecast?) }
    val isLocationAllowed = remember { mutableStateOf(true) }

    val activity = LocalContext.current as Activity

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val locationHelper =
                LocationHelper(activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager)

            val location = locationHelper.getLocation(activity)!!

            GlobalScope.launch(Dispatchers.IO) {
                val weatherAPI = APIClient.getClient().create(WeatherAPI::class.java)
                forecast.value =
                    weatherAPI.getForecast(location.latitude, location.longitude).execute().body()
            }
        }
        else {
            // Permission Denied: Do something
            Log.d("WeatherWidget", "PERMISSION DENIED")
            isLocationAllowed.value = false
        }
    }

    LaunchedEffect(null) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (forecast.value != null) {
        val hourlyForecast = forecast.value!!.hourlyForecast
        LazyColumn {
            items(hourlyForecast.timeStamps.size) {
                WeatherInfo(
                    iconResource = WeatherIcons[hourlyForecast.weatherCodes[it]]!!,
                    temperature = hourlyForecast.temperatures[it],
                    date = hourlyForecast.timeStamps[it],
                    temperatureUnit = forecast.value!!.units.temperatureUnit
                )
            }
        }
    }
    else if (isLocationAllowed.value){
        Text("Loading...")
    }
    else {
        Text("Please accept location permission to use app...")
    }
}

@Composable
fun WeatherInfo(iconResource: Int, temperature: Float, date: String, temperatureUnit: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .shadow(2.dp)
            .padding(10.dp)
    ) {
        Image(painter = painterResource(id = iconResource), contentDescription = "")
        Column {
            Text(text = temperature.toString() + temperatureUnit, fontSize = 30.sp)
            Text(date)
        }
    }
}