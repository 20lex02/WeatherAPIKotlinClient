package ca.uqac.etu.weatherapikotlinclient

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class APIClient {
    companion object {
        private var retrofit : Retrofit? = null

        fun getClient() : Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit as Retrofit
        }
    }


}

interface WeatherAPI {
    @GET("/v1/forecast?hourly=temperature_2m,weathercode")
    fun getForecast(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double
    ) : Call<WeatherForecast>
}