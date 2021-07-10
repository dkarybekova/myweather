package kg.tutorialapp.myweather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.myweather.storage.ForeCastDatabase
import kotlin.math.roundToInt

@SuppressLint("CheckResult")

class MainActivity : AppCompatActivity() {
    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeatherFromApi()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        db.forecastDao().getAll().observe(this, {
            it?.let {
                tv_temperature.text = it.current?.temp?.roundToInt().toString()
                tv_date.text = it.current?.date.format()
                tv_temp_max.text = it.daily?.get(0)?.temp?.max?.roundToInt()?.toString()
                tv_temp_min.text = it.daily?.get(0)?.temp?.min?.roundToInt()?.toString()
                tv_feels_like.text = it.current?.feels_like?.roundToInt()?.toString()
                tv_weather.text = it.current?.weather?.get(0)?.description
                tv_sunrise.text = it.current?.sunrise.format("hh:mm")
                tv_sunset.text = it.current?.sunset.format("hh:mm")
                tv_humidity.text = "${it.current?.humidity?.toString()} %"

                it.current?.weather?.get(0)?.icon?.let { icon ->
                    Glide.with(this)
                        .load("https://openweathermap.org/img/wn/${icon}@2x.png")
                        .into(iv_weather_icon)
                }
            }
        })
    }

    private fun getWeatherFromApi() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},
                {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }
}