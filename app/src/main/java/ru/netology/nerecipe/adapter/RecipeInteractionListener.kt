package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.Recipe

interface RecipeInteractionListener {
    fun favorite(recipeId: Long)
    fun navigateToRecipe(recipeId: Long)
    fun onDeleteClicked(recipeId: Long)
}
