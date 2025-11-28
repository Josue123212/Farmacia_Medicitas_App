package com.example.farmacia_medicitas.ui.screens.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.farmacia_medicitas.core.LocationService

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CheckoutScreen(
    total: Double,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val locationText = remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        TopAppBar(title = { Text(text = "Checkout") })
        Text(text = "Total a pagar: S/. ${String.format("%.2f", total)}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (LocationService.hasLocationPermission(context)) {
                val loc = LocationService.getLastKnownLocation(context)
                locationText.value = if (loc != null) "Ubicación: ${loc.latitude}, ${loc.longitude}" else "Ubicación no disponible"
            } else {
                locationText.value = "Permiso de ubicación no concedido"
            }
        }) { Text(text = "Usar ubicación para delivery") }
        if (locationText.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = locationText.value, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onConfirm) { Text(text = "Confirmar pago") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBack) { Text(text = "Volver") }
    }
}
