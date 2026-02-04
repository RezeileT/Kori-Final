package com.example.kori.data.repository

import com.example.kori.data.local.dao.DishDao
import com.example.kori.data.local.model.Dish
import kotlinx.coroutines.flow.Flow

class DishRepository(private val dishDao: DishDao) {
    
    val allDishes: Flow<List<Dish>> = dishDao.getAllDishes()
    fun getDishesByCategory(category: String): Flow<List<Dish>> = 
        dishDao.getDishesByCategory(category)
    val bestsellers: Flow<List<Dish>> = dishDao.getBestsellers()
    
    suspend fun toggleFavorite(dish: Dish) {
        dish.isFavorite = !dish.isFavorite
        dishDao.update(dish)
    }
}
