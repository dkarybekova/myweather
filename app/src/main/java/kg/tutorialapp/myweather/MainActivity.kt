package kg.tutorialapp.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kg.tutorialapp.myweather.models.ForeCast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchWeatherUsingQuery()
    }

    private fun fetchWeatherUsingQuery() {
        val call = WeatherClient.weatherApi.fetchWeatherUsingQuery(lat = 42.8746, lon = 74.5698)
        call.enqueue(object : Callback<ForeCast> {
            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {
                if (response.isSuccessful) {
                    val foreCast = response.body()
                    foreCast?.let {
                        textView.text = it.current?.temp.toString()
                        textView2.text = it.current?.weather!![0].description
                    }
                }
            }
            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}