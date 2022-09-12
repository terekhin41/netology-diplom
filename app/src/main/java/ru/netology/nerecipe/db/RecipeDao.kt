package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy
import ru.netology.nerecipe.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM allRecipes ORDER BY id DESC")
    fun getAll(): LiveData<List<AllRecipeEntity>>

    @Query("""
        SELECT * FROM allRecipes WHERE 
        favorite = 1 
        ORDER BY id DESC""")
    fun getAllFavorite(): LiveData<List<AllRecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: AllRecipeEntity)

    @Query("""
        UPDATE allRecipes SET
        favorite = CASE WHEN favorite THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun favoriteById(id: Long)

    @Query("DELETE FROM allRecipes WHERE id = :id")
    fun removeById(id: Long)

    @Query("SELECT * FROM allRecipes WHERE id = :id")
    fun getRecipeById(id: Long) : Recipe?
}