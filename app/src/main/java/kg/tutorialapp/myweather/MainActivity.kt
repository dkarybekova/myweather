package kg.tutorialapp.myweather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.myweather.models.CurrentForeCast
import kg.tutorialapp.myweather.models.ForeCast
import kg.tutorialapp.myweather.models.Weather
import kg.tutorialapp.myweather.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    private fun setup() {
        btn_insert.setOnClickListener {
            db.forecastDao()
                .insert(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
        }

        btn_update.setOnClickListener {
            db.forecastDao()
                .update(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
        }

        btn_delete.setOnClickListener {
            db.forecastDao()
                .delete(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
        }

        btn_query_get_all.setOnClickListener {
            db.forecastDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        var text = ""
                        it.forEach {
                            text += it.toString()
                        }
                        tv_forecast_list.text = text
                    },
                    {
                    })
        }

        btn_query_id.setOnClickListener {
            db.forecastDao()
                .getById(9L)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        tv_forecast_list.text = it.toString()
                    },
                    {
                    })
        }

        btn_query.setOnClickListener {
            db.forecastDao()
                .deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                    tv_forecast_list.text = null
                },
                    {
                    })
        }
    }

    private fun getForecastFromInput(): ForeCast{
        val id = et_id.text?.toString().takeIf { !it.isNullOrEmpty() }?.toLong()
        val lat = et_lat.text?.toString().takeIf { !it.isNullOrEmpty() }?.toDouble()
        val long = et_long.text?.toString().takeIf { !it.isNullOrEmpty() }?.toDouble()
        val description = et_description?.text.toString()
        val current = CurrentForeCast(weather = listOf(Weather(description = description)))

        return ForeCast(id = id, lat = lat, lon = long, current = current)
    }

    @SuppressLint("CheckResult")
    private fun makeRxCall() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }
}