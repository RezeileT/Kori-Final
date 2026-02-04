package com.example.kori.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kori.data.local.database.AppDatabase
import com.example.kori.data.local.model.Dish
import com.example.kori.data.repository.DishRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DishViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DishRepository = DishRepository(
        AppDatabase.getDatabase(application).dishDao()
    )

    val allDishes: StateFlow<List<Dish>> = repository.allDishes.stateIn(
        viewModelScope,
        kotlinx.coroutines.flow.SharingStarted.Lazily,
        emptyList()
    )

    fun getDishesByCategory(category: String): StateFlow<List<Dish>> =
        repository.getDishesByCategory(category).stateIn(
            viewModelScope,
            kotlinx.coroutines.flow.SharingStarted.Lazily,
            emptyList()
        )

    val bestsellers: StateFlow<List<Dish>> = repository.bestsellers.stateIn(
        viewModelScope,
        kotlinx.coroutines.flow.SharingStarted.Lazily,
        emptyList()
    )

    fun toggleFavorite(dish: Dish) = viewModelScope.launch {
        repository.toggleFavorite(dish)
    }
}
