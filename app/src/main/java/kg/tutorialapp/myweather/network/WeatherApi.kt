package kg.tutorialapp.myweather.network

import kg.tutorialapp.myweather.models.ForeCast
import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {

    @GET("onecall?lat=42.8746&lon=74.5698&exclude=minutely&appid=7888dd77c388dc656f9784de8954c8cb&lang=ru&units=metric")
    fun getWeather(): Call<ForeCast>
}