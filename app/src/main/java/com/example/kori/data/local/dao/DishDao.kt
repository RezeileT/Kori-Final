package com.example.kori.data.local.dao

import androidx.room.*
import com.example.kori.data.local.model.Dish
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes ORDER BY name")
    fun getAllDishes(): Flow<List<Dish>>
    
    @Query("SELECT * FROM dishes WHERE category = :category")
    fun getDishesByCategory(category: String): Flow<List<Dish>>
    
    @Query("SELECT * FROM dishes WHERE isBestseller = 1")
    fun getBestsellers(): Flow<List<Dish>>
    
    @Query("SELECT * FROM dishes WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<Dish>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishes: List<Dish>)
    
    @Update
    suspend fun update(dish: Dish)
    
    @Query("DELETE FROM dishes")
    suspend fun deleteAll()
}
