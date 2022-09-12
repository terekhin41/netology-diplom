package ru.netology.nerecipe.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nerecipe.db.AllRecipeEntity
import ru.netology.nerecipe.db.StepDao
import ru.netology.nerecipe.db.StepsEntity

@Database(
    entities = [StepsEntity::class, AllRecipeEntity::class],
    version = 1,
)
abstract class StepDB : RoomDatabase() {
    abstract val stepDao: StepDao

    companion object {
        @Volatile
        private var instance: StepDB? = null

        fun getInstance(context: Context): StepDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, StepDB::class.java, "Step.db"
            ).allowMainThreadQueries()
                .build()
    }
}