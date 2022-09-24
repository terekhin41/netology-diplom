package ru.netology.nerecipe.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.RecipesEntity
import ru.netology.nerecipe.db.StepsEntity

@Database(
    entities = [StepsEntity::class, RecipesEntity::class],
    version = 1,
)
abstract class AppDB : RoomDatabase() {
    abstract val dao: RecipeDao

    companion object {
        @Volatile
        private var instance: AppDB? = null

        fun getInstance(context: Context): AppDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDB =
            Room.databaseBuilder(context, AppDB::class.java, "App.db"
            ).allowMainThreadQueries()
                .build()
    }
}