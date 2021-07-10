package kg.tutorialapp.myweather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.myweather.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {
    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeRxCall()

        db.forecastDao().getAll().observe(this, {
            tv_forecast_list.text = it?.toString()
        })
    }

    @SuppressLint("CheckResult")
    private fun makeRxCall() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().deleteAll()
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }
}