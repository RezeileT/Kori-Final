package com.example.kori.data.local.dao

import androidx.room.*
import com.example.kori.data.local.model.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {
    @Query("SELECT * FROM reservas ORDER BY fechaCreacion DESC")
    fun getAllReservas(): Flow<List<Reserva>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reserva: Reserva)

    @Delete
    suspend fun delete(reserva: Reserva)

    @Query("SELECT COUNT(*) FROM reservas")
    suspend fun countReservas(): Int
}
