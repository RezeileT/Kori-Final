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

    private val _showEditDialog = MutableStateFlow(false)  // ← NUEVO
    val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()

    private val _reservaAEditar = MutableStateFlow<Reserva?>(null)  // ← NUEVO
    val reservaAEditar: StateFlow<Reserva?> = _reservaAEditar.asStateFlow()

    private val _reservaExitosa = MutableStateFlow(false)
    val reservaExitosa: StateFlow<Boolean> = _reservaExitosa.asStateFlow()

    private val _reservaEliminada = MutableStateFlow(false)
    val reservaEliminada: StateFlow<Boolean> = _reservaEliminada.asStateFlow()

    private val _reservaActualizada = MutableStateFlow(false)  // ← NUEVO
    val reservaActualizada: StateFlow<Boolean> = _reservaActualizada.asStateFlow()

    val todasReservas = reservaDao.getAllReservas()

    fun mostrarDialog() {
        _showDialog.value = true
    }

    fun ocultarDialog() {
        _showDialog.value = false
    }

    // ← NUEVO: Mostrar dialog de edición
    fun mostrarEditDialog(reserva: Reserva) {
        _reservaAEditar.value = reserva
        _showEditDialog.value = true
    }

    fun ocultarEditDialog() {
        _showEditDialog.value = false
        _reservaAEditar.value = null
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

    // ← NUEVO: Actualizar reserva existente
    fun actualizarReserva(
        reserva: Reserva,
        nombre: String,
        telefono: String,
        fecha: String,
        hora: String,
        personas: Int
    ) = viewModelScope.launch {
        val reservaActualizada = reserva.copy(
            nombreCompleto = nombre,
            telefono = telefono,
            fecha = fecha,
            hora = hora,
            numeroPersonas = personas
        )
        reservaDao.update(reservaActualizada)
        _showEditDialog.value = false
        _reservaAEditar.value = null
        _reservaActualizada.value = true
    }

    fun eliminarReserva(reserva: Reserva) = viewModelScope.launch {
        reservaDao.delete(reserva)
        _reservaEliminada.value = true
    }

    fun resetReservaExitosa() {
        _reservaExitosa.value = false
    }

    fun resetReservaEliminada() {
        _reservaEliminada.value = false
    }

    fun resetReservaActualizada() {  // ← NUEVO
        _reservaActualizada.value = false
    }
}
