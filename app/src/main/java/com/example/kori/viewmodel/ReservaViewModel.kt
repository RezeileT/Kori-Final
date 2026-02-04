package com.example.kori.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kori.data.local.database.AppDatabase
import com.example.kori.data.local.model.Reserva
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReservaViewModel(application: Application) : AndroidViewModel(application) {
    private val reservaDao = AppDatabase.getDatabase(application).reservaDao()
    
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    
    private val _reservaExitosa = MutableStateFlow(false)
    val reservaExitosa: StateFlow<Boolean> = _reservaExitosa.asStateFlow()
    
    val todasReservas = reservaDao.getAllReservas()
    
    fun mostrarDialog() {
        _showDialog.value = true
    }
    
    fun ocultarDialog() {
        _showDialog.value = false
    }
    
    fun guardarReserva(
        nombre: String,
        telefono: String,
        fecha: String,
        hora: String,
        personas: Int
    ) = viewModelScope.launch {
        val reserva = Reserva(
            nombreCompleto = nombre,
            email = "",
            telefono = telefono,
            fecha = fecha,
            hora = hora,
            numeroPersonas = personas,
            comentarios = ""
        )
        reservaDao.insert(reserva)
        _showDialog.value = false
        _reservaExitosa.value = true
    }
    
    fun resetReservaExitosa() {
        _reservaExitosa.value = false
    }
}
