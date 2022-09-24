package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipesEntity>>

    @Query(
        """
        SELECT * FROM recipes WHERE 
        favorite = 1 
        ORDER BY id DESC"""
    )
    fun getAllFavorite(): LiveData<List<RecipesEntity>>

    @Query(
        """
        UPDATE recipes SET
        favorite = CASE WHEN favorite THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun favoriteById(id: Long)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun loadRecipeBy(id: Long): LiveData<RecipeWithSteps?>?

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeBy(id: Long): RecipeWithSteps?

    @Transaction
    fun insert(recipeWithSteps: RecipeWithSteps) {
        insert(recipeWithSteps.recipe)
        for (step in recipeWithSteps.steps) {
            insert(step)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: RecipesEntity?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(step: StepsEntity?): Long

    @Query("SELECT MAX(id) FROM recipes")
    fun getLastIndex(): Long

    @Query("SELECT * FROM steps WHERE recipeId = :recipeId ORDER BY position ASC")
    fun getRecipeSteps(recipeId: Long): List<StepsEntity>
}