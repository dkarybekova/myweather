package kg.tutorialapp.myweather.storage

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.tutorialapp.myweather.models.DailyForeCast
import kg.tutorialapp.myweather.models.HourlyForeCast

class CollectionsConverter {
    fun fromHourlyForeCastListToJson(list: List<HourlyForeCast>): String =
        Gson().toJson(list)

    fun fromJsonToHourlyForeCastList(json: String): List<HourlyForeCast> =
        Gson().fromJson(json, object : TypeToken<List<HourlyForeCast>>() {}.type)

    fun fromDailyForeCastListToJson(list: List<DailyForeCast>): String =
        Gson().toJson(list)

    fun fromJsonToDailyForeCastList(json: String): List<DailyForeCast> =
        Gson().fromJson(json, object : TypeToken<List<DailyForeCast>>() {}.type)
}