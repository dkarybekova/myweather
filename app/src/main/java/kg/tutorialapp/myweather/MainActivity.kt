package kg.tutorialapp.myweather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.myweather.models.ForeCast
import kg.tutorialapp.myweather.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    private fun setup() {
        btn_start.setOnClickListener {
//            doSomeWork()
            makeRxCall()
        }
        btn_show_toast.setOnClickListener {
            Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()

            ForeCastDatabase.getInstance(applicationContext).forecastDao().insert(ForeCast(lat = 21341.000))
        }
    }

    @SuppressLint("CheckResult")
    private fun makeRxCall() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textView.text = it.current?.temp.toString()
                textView2.text = it.current?.weather!![0].description
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

            })
    }

        //just, create, fromCallable(), fromIterable()
        //disposable, compositeDisposable, clear(), dispose()
        //map, flatmap, zip

    private fun doSomeWork() {
        val observable = Observable.create<String>  { emitter->
            Log.d(TAG, "${Thread.currentThread().name} starting emitting")
            Thread.sleep(3000)
            emitter.onNext("Hello")
            Thread.sleep(1000)
            emitter.onNext("Bishkek")
            emitter.onComplete()
        }
        val observer = object: Observer<String>{
            override fun onSubscribe(d: Disposable) {
            }
            override fun onNext(t: String) {
                Log.d(TAG, "${Thread.currentThread().name} onNext{} $t")
            }
            override fun onError(e: Throwable) {
            }
            override fun onComplete() {
            }
        }
        observable
            .subscribeOn(Schedulers.computation())
            .map {
                Log.d(TAG, "${Thread.currentThread().name} starting mapping")
                it.uppercase()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
    companion object{
        const val TAG = "Rx"
    }
}