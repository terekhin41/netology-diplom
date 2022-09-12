package ru.netology.nerecipe.db

import androidx.room.*
import androidx.room.OnConflictStrategy

@Dao
interface StepDao {
    /*@Query("SELECT * FROM steps ORDER BY position DESC")
    fun getAll(): LiveData<List<StepsEntity>>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(step: StepsEntity)

    /*@Query("DELETE FROM steps WHERE position = :position AND recipeId = :recipeId")
    fun removeStepByPosition(position: Int, recipeId: Long)*/

    @Query("SELECT * FROM steps WHERE recipeId = :recipeId ORDER BY position ASC")
    fun getRecipeSteps(recipeId: Long): List<StepsEntity>
}