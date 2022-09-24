package ru.netology.nerecipe.db

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithSteps(
    @Embedded
    val recipe : RecipesEntity,
    @Relation(parentColumn = "id", entity = StepsEntity::class, entityColumn = "recipeId")
    val steps : List<StepsEntity>
)