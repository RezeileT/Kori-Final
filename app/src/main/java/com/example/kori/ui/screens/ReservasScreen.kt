package com.example.kori.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kori.data.local.model.Reserva
import com.example.kori.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(
    viewModel: ReservaViewModel = viewModel()
) {
    val showDialog by viewModel.showDialog.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()
    val reservaAEditar by viewModel.reservaAEditar.collectAsState()
    val reservaExitosa by viewModel.reservaExitosa.collectAsState()
    val reservaEliminada by viewModel.reservaEliminada.collectAsState()
    val reservaActualizada by viewModel.reservaActualizada.collectAsState()
    val reservas by viewModel.todasReservas.collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(reservaExitosa) {
        if (reservaExitosa) {
            snackbarHostState.showSnackbar("✅ Reserva guardada correctamente")
            viewModel.resetReservaExitosa()
        }
    }

    LaunchedEffect(reservaEliminada) {
        if (reservaEliminada) {
            snackbarHostState.showSnackbar("🗑️ Reserva eliminada")
            viewModel.resetReservaEliminada()
        }
    }

    LaunchedEffect(reservaActualizada) {
        if (reservaActualizada) {
            snackbarHostState.showSnackbar("✏️ Reserva actualizada")
            viewModel.resetReservaActualizada()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas KÖRI") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.mostrarDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Reserva")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (reservas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No hay reservas. Pulsa + para crear una",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(
                    items = reservas,
                    key = { it.id }
                ) { reserva ->
                    ReservaCard(
                        reserva = reserva,
                        onEdit = { viewModel.mostrarEditDialog(reserva) },
                        onDelete = {
                            reservaAEliminar = reserva
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Dialog crear nueva reserva
    if (showDialog) {
        DialogNuevaReserva(
            onDismiss = { viewModel.ocultarDialog() },
            onConfirm = { nombre, telefono, fecha, hora, personas ->
                viewModel.guardarReserva(nombre, telefono, fecha, hora, personas)
            }
        )
    }

    // Dialog editar reserva
    if (showEditDialog && reservaAEditar != null) {
        DialogEditarReserva(
            reserva = reservaAEditar!!,
            onDismiss = { viewModel.ocultarEditDialog() },
            onConfirm = { nombre, telefono, fecha, hora, personas ->
                viewModel.actualizarReserva(
                    reservaAEditar!!,
                    nombre,
                    telefono,
                    fecha,
                    hora,
                    personas
                )
            }
        )
    }

    // Dialog confirmar eliminación
    if (showDeleteDialog && reservaAEliminar != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.Delete, contentDescription = null) },
            title = { Text("¿Eliminar reserva?") },
            text = {
                Text("Se eliminará la reserva de ${reservaAEliminar?.nombreCompleto}")
            },
            confirmButton = {
                Button(
                    onClick = {
                        reservaAEliminar?.let { viewModel.eliminarReserva(it) }
                        showDeleteDialog = false
                        reservaAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reserva.nombreCompleto,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "📞 ${reserva.telefono}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "📅 ${reserva.fecha} • ${reserva.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${reserva.numeroPersonas} pax",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Botón editar
                IconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar reserva"
                    )
                }

                // Botón eliminar
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar reserva"
                    )
                }
            }
        }
    }
}

@Composable
fun DialogNuevaReserva(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("2026-02-04") }
    var hora by remember { mutableStateOf("20:00") }
    var personas by remember { mutableStateOf("2") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Reserva") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it.filter { c -> c.isDigit() } },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = personas,
                    onValueChange = { personas = it.filter { c -> c.isDigit() } },
                    label = { Text("Personas") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && telefono.isNotBlank()) {
                        onConfirm(
                            nombre,
                            telefono,
                            fecha,
                            hora,
                            personas.toIntOrNull() ?: 2
                        )
                    }
                },
                enabled = nombre.isNotBlank() && telefono.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogEditarReserva(
    reserva: Reserva,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Int) -> Unit
) {
    var nombre by remember { mutableStateOf(reserva.nombreCompleto) }
    var telefono by remember { mutableStateOf(reserva.telefono) }
    var fecha by remember { mutableStateOf(reserva.fecha) }
    var hora by remember { mutableStateOf(reserva.hora) }
    var personas by remember { mutableStateOf(reserva.numeroPersonas.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Edit, contentDescription = null) },
        title = { Text("Editar Reserva") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it.filter { c -> c.isDigit() } },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = personas,
                    onValueChange = { personas = it.filter { c -> c.isDigit() } },
                    label = { Text("Personas") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && telefono.isNotBlank()) {
                        onConfirm(
                            nombre,
                            telefono,
                            fecha,
                            hora,
                            personas.toIntOrNull() ?: 2
                        )
                    }
                },
                enabled = nombre.isNotBlank() && telefono.isNotBlank()
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
