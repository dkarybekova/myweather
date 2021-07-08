package kg.tutorialapp.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kg.tutorialapp.myweather.models.ForeCast
import kg.tutorialapp.myweather.models.Post
import kg.tutorialapp.myweather.network.PostsApi
import kg.tutorialapp.myweather.network.WeatherApi
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //fetchWeather()
        //fetchWeatherUsingQuery()
        //fetchPostById()
        //createPost()
        //createPostUsingFields()
        createPostUsingFieldsMap()
    }

    private fun createPostUsingFieldsMap() {
        val map = HashMap<String, String>().apply {
            put("userId", "55")
            put("title", "SUF!!!")
            put("body", "Karakol")
        }

        val call = postsApi.createPostUsingFieldsMap(map)

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()
                resultPost?.let {
                    val resultText = "ID:" + it.id + "\n" +
                            "userID:" + it.userId + "\n" +
                            "TITLE:" + it.title + "\n" +
                            "BODY:" + it.body + "\n"
                    textView.text = resultText
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
            }
        })
    }

    private fun createPostUsingFields() {
        val call = postsApi.createPostUsingFields(userId = "99", title = "HI!!!", body = "Osh")
        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()
                resultPost?.let {
                    val resultText = "ID:" + it.id + "\n" +
                            "userID:" + it.userId + "\n" +
                            "TITLE:" + it.title + "\n" +
                            "BODY:" + it.body + "\n"
                    textView.text = resultText
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
            }
        })
    }

    private fun createPost() {
        val post = Post(userId = 42, title = "Hello", body = "BISHKEK")
        val call = postsApi.createPost(post)
        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()
                resultPost?.let {
                    val resultText = "ID:" + it.id + "\n" +
                            "userID:" + it.userId + "\n" +
                            "TITLE:" + it.title + "\n" +
                            "BODY:" + it.body + "\n"
                    textView.text = resultText
                }
            }
            override fun onFailure(call: Call<Post>, t: Throwable) {
            }
        })
    }

    private fun fetchPostById() {
        val call = postsApi.fetchPostById(10)
        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val post = response.body()
                post?.let {
                    val resultText = "ID:" + it.id + "\n" +
                            "userID:" + it.userId + "\n" +
                            "TITLE:" + it.title + "\n" +
                            "BODY:" + it.body + "\n"
                    textView.text = resultText
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {


            }
        })
    }

    private fun fetchWeatherUsingQuery() {
        val call = weatherApi.fetchWeatherUsingQuery(lat = 	40.513999, lon = 72.816098)
        call.enqueue(object : Callback<ForeCast> {
            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {
                if (response.isSuccessful) {
                    val foreCast = response.body()
                    foreCast?.let {
                        textView.text = it.current?.weather!![0].description
                        textView2.text = it.current?.temp.toString()
                        //Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun fetchWeather() {
        val call = weatherApi.fetchWeather()
        call.enqueue(object : Callback<ForeCast> {
            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {
                if (response.isSuccessful) {
                    val foreCast = response.body()
                    foreCast?.let {
                        textView.text = it.current?.weather!![0].description
                        textView2.text = it.current?.temp.toString()
//                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private val okHttp by lazy {
        val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            //.baseUrl("https://api.openweathermap.org/data/2.5/")
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttp)
            .build()
    }

    private val weatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

    private  val postsApi by lazy {
        retrofit.create(PostsApi::class.java)
    }
}