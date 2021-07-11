package kg.tutorialapp.myweather.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.myweather.R
import kg.tutorialapp.myweather.network.WeatherClient
import kg.tutorialapp.myweather.format
import kg.tutorialapp.myweather.models.Constants
import kg.tutorialapp.myweather.models.ForeCast
import kg.tutorialapp.myweather.storage.ForeCastDatabase
import kg.tutorialapp.myweather.ui.rv.DailyForeCastAdapter
import kg.tutorialapp.myweather.ui.rv.HourlyForeCastAdapter
import kotlin.math.roundToInt

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    private lateinit var dailyForeCastAdapter: DailyForeCastAdapter
    private lateinit var hourlyForeCastAdapter: HourlyForeCastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupRecyclerViews()
        getWeatherFromApi()
        subscribeToLiveData()
    }

    private fun setupViews() {
        tv_refresh.setOnClickListener {
            showLoading()
            getWeatherFromApi()
        }
    }

    private fun setupRecyclerViews() {
        dailyForeCastAdapter = DailyForeCastAdapter()
        rv_daily_forecast.adapter = dailyForeCastAdapter

        hourlyForeCastAdapter = HourlyForeCastAdapter()
        rv_hourly_forecast.adapter = hourlyForeCastAdapter
    }

    private fun showLoading() {
        progress.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        progress.visibility = View.GONE
    }

    private fun getWeatherFromApi() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
            },
                {
                    hideLoading()
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                })
    }

    private fun subscribeToLiveData() {
        db.forecastDao().getAll().observe(this, {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)
                it.daily?.let { dailyList ->
                    dailyForeCastAdapter.setItems(dailyList) }
                it.hourly?.let { hourlyList ->
                    hourlyForeCastAdapter.setItems(hourlyList) }
            }
        })
    }

    private fun setValuesToViews(it: ForeCast) {
        tv_temperature.text = it.current?.temp?.roundToInt().toString()
        tv_date.text = it.current?.date.format()
        tv_temp_max.text = it.daily?.get(0)?.temp?.max?.roundToInt()?.toString()
        tv_temp_min.text = it.daily?.get(0)?.temp?.min?.roundToInt()?.toString()
        tv_feels_like.text = it.current?.feels_like?.roundToInt()?.toString()
        tv_weather.text = it.current?.weather?.get(0)?.description
        tv_sunrise.text = it.current?.sunrise.format("HH:mm")
        tv_sunset.text = it.current?.sunset.format("HH:mm")
        tv_humidity.text = "${it.current?.humidity?.toString()} %"
    }

    private fun loadWeatherIcon(it: ForeCast) {
        it.current?.weather?.get(0)?.icon?.let { icon ->
            Glide.with(this)
                .load("${Constants.iconUri}${icon}${Constants.iconFormat}")
                .into(iv_weather_icon)
        }
    }
}

