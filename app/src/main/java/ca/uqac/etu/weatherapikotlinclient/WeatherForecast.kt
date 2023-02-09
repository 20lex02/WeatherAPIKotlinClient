package ca.uqac.etu.weatherapikotlinclient

import com.google.gson.annotations.SerializedName
import java.util.*

class WeatherForecast {
    @SerializedName("hourly_units")
    val units: Units = Units()

    @SerializedName("hourly")
    val hourlyForecast: HourlyForecast = HourlyForecast()
}

class HourlyForecast {
    @SerializedName("time")
    val timeStamps: List<String> = emptyList()

    @SerializedName("temperature_2m")
    val temperatures: List<Float> = emptyList()

    @SerializedName("weathercode")
    val weatherCodes: List<Int> = emptyList()
}

class Units {
    @SerializedName("temperature_2m")
    val temperatureUnit: String = ""
}