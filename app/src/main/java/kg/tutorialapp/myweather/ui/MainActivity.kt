package kg.tutorialapp.myweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kg.tutorialapp.myweather.R
import kg.tutorialapp.myweather.format
import kg.tutorialapp.myweather.models.Constants
import kg.tutorialapp.myweather.models.ForeCast
import kg.tutorialapp.myweather.ui.rv.DailyForeCastAdapter
import kg.tutorialapp.myweather.ui.rv.HourlyForeCastAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var vm: MainViewModel
    private lateinit var dailyForeCastAdapter: DailyForeCastAdapter
    private lateinit var hourlyForeCastAdapter: HourlyForeCastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = getViewModel (MainViewModel::class)
        setupViews()
        setupRecyclerViews()
        subscribeToLiveData()
    }

    private fun setupViews() {
        tv_refresh.setOnClickListener {
            vm.showLoading()
            vm.getWeatherFromApi()
        }
    }

    private fun setupRecyclerViews() {
        dailyForeCastAdapter = DailyForeCastAdapter()
        rv_daily_forecast.adapter = dailyForeCastAdapter

        hourlyForeCastAdapter = HourlyForeCastAdapter()
        rv_hourly_forecast.adapter = hourlyForeCastAdapter
    }

    private fun subscribeToLiveData() {
        vm.getForeCastAsLive().observe(this, {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)
                setDataToRecyclerViews(it)
            }
        })

        vm._isLoading.observe(this, Observer {
            when(it){
                true -> showLoading()
                false -> hideLoading()
            }
        })
    }

    private fun setDataToRecyclerViews(it: ForeCast) {
        it.daily?.let { dailyList ->
            dailyForeCastAdapter.setItems(dailyList)
        }
        it.hourly?.let { hourlyList ->
            hourlyForeCastAdapter.setItems(hourlyList)
        }
    }

    private fun showLoading() {
        progress.post {
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading(){
        progress.postDelayed({
            progress.visibility = View.INVISIBLE
        }, 2000)
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

