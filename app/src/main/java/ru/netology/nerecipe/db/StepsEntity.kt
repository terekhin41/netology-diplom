package ru.netology.nerecipe.db

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "steps",
    foreignKeys = [
        ForeignKey(entity = AllRecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
class StepsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "text")
    val text: String,
    @Nullable
    @ColumnInfo(name = "imgPath")
    val imgPath: String?,
    @ColumnInfo(name = "recipeId")
    val recipeId: Long
)