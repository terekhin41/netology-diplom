package ru.netology.nerecipe.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nerecipe.db.AllRecipeEntity
import ru.netology.nerecipe.db.RecipeDao

@Database(
    entities = [AllRecipeEntity::class],
    version = 1
)
abstract class AllRecipeDB : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var instance: AllRecipeDB? = null

        fun getInstance(context: Context): AllRecipeDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AllRecipeDB::class.java, "AllRecipe.db"
            ).allowMainThreadQueries()
                .build()
    }
}