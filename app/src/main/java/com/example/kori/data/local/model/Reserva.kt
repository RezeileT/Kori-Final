package com.example.kori.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "reservas")
data class Reserva(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreCompleto: String,  // ← Cambié "nombre" por "nombreCompleto"
    val email: String,
    val telefono: String,
    val fecha: String,        // "2026-02-04"
    val hora: String,         // "20:30"
    val numeroPersonas: Int,  // ← Cambié "personas" por "numeroPersonas"
    val comentarios: String = "",  // ← AÑADIDO campo comentarios
    val fechaCreacion: Long = System.currentTimeMillis()
) {
    fun getFechaFormateada(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(fechaCreacion))
    }
}
