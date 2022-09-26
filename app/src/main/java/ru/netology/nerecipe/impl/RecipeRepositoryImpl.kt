package ru.netology.nerecipe.impl

import androidx.lifecycle.map
import ru.netology.nerecipe.Category
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.db.RecipeDao
import ru.netology.nerecipe.db.RecipeWithSteps
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override val allRecipeData = recipeDao.getAll().map { entities ->
        entities.map { it.toModel() }
    }
    override val favoriteRecipeData = recipeDao.getAllFavorite().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(recipe: Recipe, steps: List<Step>) {
        val stepsEntities = steps.map { it.toEntity() }
        val recipeWithSteps = RecipeWithSteps(recipe.toEntity(), stepsEntities)
        recipeDao.insert(recipeWithSteps)
    }

    override fun favorite(recipeId: Long) {
        recipeDao.favoriteById(recipeId)
    }

    override fun delete(recipeId: Long) {
        recipeDao.removeById(recipeId)
    }

    override fun getRecipeById(id: Long): RecipeWithSteps? {
         return recipeDao.getRecipeBy(id)
    }

    override fun addDefaultData() {
        defaultData.forEach { recipeDao.insert(it) }
    }

    override fun getLastRecipeId() : Long {
        return recipeDao.getLastIndex()
    }

    private val defaultData = listOf(
        RecipeWithSteps(
            Recipe(1L, "Первый рецепт", Category.AMERICAN.key,"Автор1").toEntity(),
            listOf(
                Step(1L,0,"1").toEntity(),
                Step(1L,1,"2").toEntity()
            )
        ),
        RecipeWithSteps(
            Recipe(2L, "Второй рецепт", Category.EUROPEAN.key,"Автор2").toEntity(),
            listOf(
                Step(2L,0,"1").toEntity(),
                Step(2L,1,"2").toEntity()
            )
        ),
        RecipeWithSteps(
            Recipe(3L, "Третий рецепт", Category.EASTERN.key,"Автор3").toEntity(),
            listOf(
                Step(3L,0,"1").toEntity(),
                Step(3L,1,"2").toEntity()
            )
        ),
        RecipeWithSteps(
            Recipe(4L, "Четвертый рецепт", Category.ASIAN.key,"Автор4").toEntity(),
            listOf(
                Step(4L,0,"1").toEntity(),
                Step(4L,1,"2").toEntity()
            )
        )
    )
}