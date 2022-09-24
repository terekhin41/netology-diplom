package ru.netology.nerecipe.impl

import androidx.lifecycle.LiveData
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.db.RecipeWithSteps

interface RecipeRepository {
    val allRecipeData: LiveData<List<Recipe>>
    val favoriteRecipeData: LiveData<List<Recipe>>

    fun delete(recipeId: Long)

    fun save(recipe: Recipe, steps: List<Step>)

    fun favorite(recipeId: Long)

    fun getRecipeById(id: Long) : RecipeWithSteps?

    fun addDefaultData()

    fun getLastRecipeId() : Long
}