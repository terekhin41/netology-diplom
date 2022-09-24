package ru.netology.nerecipe.db

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "steps",
    foreignKeys = [
        ForeignKey(entity = RecipesEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ],
    primaryKeys = [ "recipeId", "position" ]
)
class StepsEntity(
    @ColumnInfo(name = "recipeId")
    val recipeId: Long,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "text")
    val text: String,
    @Nullable
    @ColumnInfo(name = "imgPath")
    val imgPath: String?
)