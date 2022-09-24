package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nerecipe.Category
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.adapter.RecipeInteractionListener
import ru.netology.nerecipe.adapter.StepInteractionListener
import ru.netology.nerecipe.db.toModel
import ru.netology.nerecipe.impl.AppDB
import ru.netology.nerecipe.impl.RecipeRepository
import ru.netology.nerecipe.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.util.SingleLiveEvent

class RecipeViewModel (application: Application
) : AndroidViewModel(application),
    StepInteractionListener,
    RecipeInteractionListener {

    private val repository: RecipeRepository =
        RecipeRepositoryImpl(
            recipeDao = AppDB.getInstance(
                context = application
            ).dao
        )

    val allRecipeData by repository::allRecipeData
    val favoriteRecipeData by repository::favoriteRecipeData

    val navigateToRecipe = SingleLiveEvent<Long>()
    val indexedSteps = SingleLiveEvent<List<Step>>()
    val filteredAllRecipesList = SingleLiveEvent<List<Recipe>>()
    val filteredFavoriteRecipesList = SingleLiveEvent<List<Recipe>>()
    val recipesFilters = SingleLiveEvent<MutableMap<Category, Boolean>>()
    var currentRecipe : Recipe? = null

    init {
        val filters = mutableMapOf<Category, Boolean>()
        Category.values().forEach { filters[it] = true }
        recipesFilters.value = filters
    }


    fun createIndexedStep(position: Int, text: String, imgPath: String? = null) {
        val step = Step(
            position = position,
            text = text,
            imgPath = imgPath)
        indexedSteps.value = indexedSteps.value?.plus(listOf(step)) ?: listOf(step)
    }

    override fun indexedStepDelete(step: Step) {
        indexedSteps.value = indexedSteps.value
            ?.filter { it != step }
            ?.mapIndexed { position, oldStep ->
                oldStep.copy(position = position) }
    }

    fun saveRecipe(title: String, category: String, author: String) {
        val recipe = currentRecipe?.copy(
            title = title,
            category = category,
            author = author,
        ) ?: Recipe(
            id = repository.getLastRecipeId() + 1L,
            title = title,
            category = category,
            author = author
        )
        val steps = indexedSteps.value?.map { it.copy(recipeId = recipe.id) } ?: emptyList()
        repository.save(recipe, steps)
        currentRecipe = null
        indexedSteps.value = emptyList()
    }

    override fun favorite(recipeId: Long) {
        repository.favorite(recipeId)
    }

    override fun navigateToRecipe(recipeId: Long) {
        navigateToRecipe.value = recipeId
    }

    override fun onDeleteClicked(recipeId: Long) {
        repository.delete(recipeId)
    }

    fun addDefaultData() {
        if (allRecipeData.value == null || allRecipeData.value?.size == 0)
            repository.addDefaultData()
    }

    fun searchAllRecipes(query: String?) {
        if (query != null) {
            filteredAllRecipesList.value = allRecipeData.value
                ?.filter { it.title.contains(query, ignoreCase = true) }
                .also { filterAllRecipes(it) }
        } else {
            filteredAllRecipesList.value = allRecipeData.value
            filterAllRecipes()
        }
    }

    fun searchFavoriteRecipes(query: String?) {
        if (query != null) {
            filteredFavoriteRecipesList.value = favoriteRecipeData.value
                ?.filter { it.title.contains(query, ignoreCase = true) }
                .also { filterFavoriteRecipes(it) }
        } else {
            filteredFavoriteRecipesList.value = favoriteRecipeData.value
            filterFavoriteRecipes()
        }
    }

    fun filterAllRecipes(currentList: List<Recipe>? = null) {
        val result = currentList ?: allRecipeData.value
        filteredAllRecipesList.value = result?.filter { recipe ->
            lateinit var category : Category
            Category.values().forEach { if (it.key == recipe.category) category = it }
            recipesFilters.value?.get(category) ?: true
        } ?: return
    }

    fun filterFavoriteRecipes(currentList: List<Recipe>? = null) {
        val result = currentList ?: favoriteRecipeData.value
        filteredFavoriteRecipesList.value = result?.filter { recipe ->
            lateinit var category : Category
            Category.values().forEach { if (it.key == recipe.category) category = it }
            recipesFilters.value?.get(category) ?: true
        } ?: return
    }

    fun getRecipeById(id: Long) : Recipe? {
        return repository.getRecipeById(id)?.recipe?.toModel()
    }

    fun getRecipesStepsById(id: Long) : List<Step>? {
        return repository.getRecipeById(id)?.steps?.map { it.toModel() }
    }
}