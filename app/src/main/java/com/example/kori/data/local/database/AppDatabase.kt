package com.example.kori.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kori.data.local.dao.DishDao
import com.example.kori.data.local.model.Dish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Dish::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kori_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateDatabase(database.dishDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDatabase(dishDao: DishDao) {
            val dishes = listOf(
                Dish(
                    name = "Sushi Premium",
                    description = "Selección especial de sushi fresco",
                    price = 28.50,
                    imageUrl = "https://picsum.photos/300/200?random=1",
                    category = "ITEM1",
                    isFavorite = true
                ),
                Dish(
                    name = "Ramen Clásico",
                    description = "Caldo casero con chashu",
                    price = 18.90,
                    imageUrl = "https://picsum.photos/300/200?random=2",
                    category = "ITEM2"
                ),
                Dish(
                    name = "Tempura Mixta",
                    description = "Langostinos y verduras crujientes",
                    price = 22.00,
                    imageUrl = "https://picsum.photos/300/200?random=3",
                    category = "ITEM3",
                    isFavorite = true
                )
            )
            dishDao.insertAll(dishes)
        }
    }
}
