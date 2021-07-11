package kg.tutorialapp.myweather.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.tutorialapp.myweather.models.DailyForeCast
import kg.tutorialapp.myweather.R
import kg.tutorialapp.myweather.format
import kg.tutorialapp.myweather.models.Constants
import kotlin.math.roundToInt
import kotlinx.android.synthetic.main.item_daily_forecast.view.*

class DailyForeCastVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DailyForeCast){
        itemView.run {
            tv_weekday.text = item.date.format("dd/MM")
            item.probability?.let {
                tv_precipitation.text = "${(it * 100).roundToInt()} %"
            }
            tv_temp_max.text = item.temp?.max?.roundToInt()?.toString()
            tv_temp_min.text = item.temp?.min?.roundToInt()?.toString()

            Glide.with(context)
                .load("${Constants.iconUri}${item.weather?.get(0)?.icon}${Constants.iconFormat}")
                .into(iv_weather_icon)
        }
    }

    companion object{
        fun create(parent: ViewGroup): DailyForeCastVH{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daily_forecast, parent, false)

            return DailyForeCastVH(view)
        }
    }
}