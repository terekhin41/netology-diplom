package ru.netology.nerecipe.db

import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step

internal fun RecipesEntity.toModel() = Recipe(
    id = id,
    title = title,
    category = category,
    author = author,
    favorite = favorite
)

internal fun Recipe.toEntity() = RecipesEntity(
    id = id,
    title = title,
    category = category,
    author = author,
    favorite = favorite
)

internal fun StepsEntity.toModel() = Step(
    recipeId = recipeId,
    position = position,
    text = text,
    imgPath = imgPath
)

internal fun Step.toEntity() = StepsEntity(
    recipeId = recipeId,
    position = position,
    text = text,
    imgPath = imgPath
)