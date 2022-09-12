package ru.netology.nerecipe

import androidx.room.Ignore
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long,
    val title: String,
    val category: String,
    val author: String,
    val favorite: Boolean = false
    ) {
    @Ignore
    lateinit var steps: List<Step>
}

@Serializable
data class Step(
    val id: Long,
    val position: Int,
    val text: String,
    val imgPath: String? = null
    )

enum class Category(
    val key: String
) {
    EUROPEAN("Европейская"),
    ASIAN("Азиатская"),
    PAN_ASIAN("Паназиатская"),
    EASTERN("Восточная"),
    AMERICAN("Американская"),
    RUSSIAN("Русская"),
    MEDITERRANEAN("Средиземноморская");

    companion object {
        const val KEY = "category"
    }
}
