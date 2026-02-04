package com.example.kori.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kori.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(
    viewModel: ReservaViewModel = viewModel()
) {
    val showDialog by viewModel.showDialog.collectAsState()
    val reservaExitosa by viewModel.reservaExitosa.collectAsState()
    val reservas by viewModel.todasReservas.collectAsState(initial = emptyList())
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(reservaExitosa) {
        if (reservaExitosa) {
            snackbarHostState.showSnackbar("✅ Reserva guardada correctamente")
            viewModel.resetReservaExitosa()
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
                items(reservas) { reserva ->
                    ReservaCard(reserva)
                }
            }
        }
    }
    
    if (showDialog) {
        DialogNuevaReserva(
            onDismiss = { viewModel.ocultarDialog() },
            onConfirm = { nombre, telefono, fecha, hora, personas ->
                viewModel.guardarReserva(nombre, telefono, fecha, hora, personas)
            }
        )
    }
}

@Composable
fun ReservaCard(reserva: com.example.kori.data.local.model.Reserva) {
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
