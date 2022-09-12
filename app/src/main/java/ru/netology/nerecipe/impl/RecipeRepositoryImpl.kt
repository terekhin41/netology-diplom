package ru.netology.nerecipe.impl

import androidx.lifecycle.map
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.StepDao
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel
import ru.netology.nerecipe.impl.RecipeRepository.Companion.NEW_RECIPE_ID
import java.sql.SQLException

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao,
    private val stepDao: StepDao
) : RecipeRepository {

    override val allRecipeData = recipeDao.getAll().map { entities ->
        entities.map { it.toModel() } //может в toModel Прописать присоединение Steps
    }
    override val favoriteRecipeData = recipeDao.getAllFavorite().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(recipe: Recipe) {
        if (recipe.id != NEW_RECIPE_ID) recipeDao.removeById(recipe.id)
        recipeDao.insert(recipe.toEntity())
        recipe.steps.forEachIndexed { index, step ->
            stepDao.insert(step.toEntity(0L, index))
        }
    }

    override fun favorite(recipe: Recipe) {
        recipeDao.favoriteById(recipe.id)
    }

    override fun delete(recipeId: Long) {
        recipeDao.removeById(recipeId)
    }

    override fun getRecipeById(id: Long): Recipe? {
         return recipeDao.getRecipeById(id)
    }

    override fun getSteps(recipe: Recipe): List<Step> {
        return try {
            stepDao.getRecipeSteps(recipe.id).map { it.toModel() }
        } catch (ex: SQLException) {
            emptyList()
        }
    }
}