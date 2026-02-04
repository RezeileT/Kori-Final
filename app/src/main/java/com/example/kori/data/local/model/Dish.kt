package com.example.kori.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,  // Para Coil
    val category: String,  // "ITEM1", "ITEM2", "ITEM3"
    val isBestseller: Boolean = false,
    var isFavorite: Boolean = false

)
