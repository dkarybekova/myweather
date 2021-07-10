package kg.tutorialapp.myweather.storage

import android.content.Context
import androidx.room.*
import kg.tutorialapp.myweather.models.ForeCast

@Database(
    entities = [ForeCast::class],
    version = 3,
    exportSchema = false
)

@TypeConverters(ModelsConverter::class, CollectionsConverter::class)
abstract class ForeCastDatabase: RoomDatabase() {
    abstract fun forecastDao(): ForeCastDao

    companion object{
        private const val DB_NAME = "forecastDB"
        private var DB: ForeCastDatabase? = null

        fun getInstance(context: Context): ForeCastDatabase{
            if (DB == null){
                DB = Room.databaseBuilder(
                    context,
                    ForeCastDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return DB!!
        }
    }
}