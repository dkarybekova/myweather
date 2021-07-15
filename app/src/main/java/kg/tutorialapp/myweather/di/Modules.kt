package kg.tutorialapp.myweather.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import kg.tutorialapp.myweather.network.WeatherApi
import kg.tutorialapp.myweather.repo.WeatherRepo
import kg.tutorialapp.myweather.storage.ForeCastDatabase
import kg.tutorialapp.myweather.ui.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val vmModule = module {
    viewModel { MainViewModel(get())}
}

val dataModule = module{
    single { provideForeCastDatabase(androidApplication()) }
    single { provideHttpClient() }
    single { provideRetrofit(get()) }
    factory { provideWeatherApi(get()) }
    factory { WeatherRepo(get(), get()) }
}


fun provideForeCastDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        ForeCastDatabase::class.java,
        ForeCastDatabase.DB_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

private fun provideHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    return OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
}

private fun provideRetrofit(httpClient: OkHttpClient) =
    Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

private fun provideWeatherApi(retrofit: Retrofit) = retrofit.create(WeatherApi::class.java)