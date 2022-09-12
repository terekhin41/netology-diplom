package ru.netology.nerecipe.db

import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step

internal fun AllRecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    category = category,
    author = author,
    favorite = favorite
)

internal fun Recipe.toEntity() = AllRecipeEntity(
    id = id,
    title = title,
    category = category,
    author = author,
    favorite = favorite
)

internal fun StepsEntity.toModel() = Step(
    id = id,
    position = position,
    text = text,
    imgPath = imgPath
)

internal fun Step.toEntity(
    recipeId: Long,
    position: Int
) = StepsEntity(
    id = id,
    recipeId = recipeId,
    position = position,
    text = text,
    imgPath = imgPath
)