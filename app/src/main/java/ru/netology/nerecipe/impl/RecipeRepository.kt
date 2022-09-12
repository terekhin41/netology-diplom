package ru.netology.nerecipe.impl

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step

interface RecipeRepository {
    val allRecipeData: LiveData<List<Recipe>>
    val favoriteRecipeData: LiveData<List<Recipe>>

    fun delete(recipeId: Long)

    fun save(recipe: Recipe)

    fun favorite(recipe: Recipe)

    fun getRecipeById(id: Long) : Recipe?

    fun getSteps(recipe: Recipe) : List<Step>

    companion object {
        const val NEW_RECIPE_ID = 0L
        const val NEW_STEP_ID = 0L
    }
}