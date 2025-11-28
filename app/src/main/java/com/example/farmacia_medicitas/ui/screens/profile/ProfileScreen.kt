package com.example.farmacia_medicitas.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.farmacia_medicitas.ui.viewmodel.AuthViewModel
import com.example.farmacia_medicitas.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onLogoutNavigate: () -> Unit
) {
    val vm: AuthViewModel = hiltViewModel()
    val state by vm.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás") }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier.padding(inner).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(64.dp))
                }
            }
            Spacer(Modifier.height(12.dp))

            if (state.isAuthenticated) {
                val user = state.user
                Text(
                    user?.let { (it.first_name ?: "") + " " + (it.last_name ?: "") }
                        ?.trim()
                        ?.takeIf { it.isNotBlank() }
                        ?: (user?.username ?: "Usuario"),
                    style = MaterialTheme.typography.titleMedium
                )
                if (!user?.email.isNullOrBlank()) {
                    Text(user!!.email!!, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (!user?.role.isNullOrBlank()) {
                    Text("Rol: ${user!!.role}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(Modifier.height(24.dp))
                Button(onClick = {
                    vm.logout()
                    onLogoutNavigate()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar sesión")
                }

                // Herramientas de prueba (solo en debug): probar endpoint protegido y forzar 401 para refresco
                if (BuildConfig.DEBUG) {
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = { vm.checkSession() }, modifier = Modifier.weight(1f)) { Text("Probar sesión (me)") }
                        Button(onClick = { vm.invalidateAccessAndCheckForDebug() }, modifier = Modifier.weight(1f)) { Text("Forzar 401 y reintentar") }
                    }
                    if (!state.sessionCheckMessage.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(state.sessionCheckMessage!!, color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text("Opciones", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ElevatedCard { ListItem(headlineContent = { Text("Mis pedidos") }, supportingContent = { Text("Historial de compras") }) }
                    ElevatedCard { ListItem(headlineContent = { Text("Direcciones") }, supportingContent = { Text("Gestiona tus direcciones") }) }
                    ElevatedCard { ListItem(headlineContent = { Text("Métodos de pago") }, supportingContent = { Text("Tarjetas y pagos") }) }
                }
            } else {
                Text("Invitado", style = MaterialTheme.typography.titleMedium)
                Text("Inicia sesión para ver tus pedidos y datos", color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onLogin, modifier = Modifier.weight(1f)) { Text("Iniciar sesión") }
                    Button(onClick = onRegister, modifier = Modifier.weight(1f)) { Text("Registrarse") }
                }

                Spacer(Modifier.height(32.dp))
                Text("Opciones", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ElevatedCard { ListItem(headlineContent = { Text("Mis pedidos") }, supportingContent = { Text("Historial de compras") }) }
                    ElevatedCard { ListItem(headlineContent = { Text("Direcciones") }, supportingContent = { Text("Gestiona tus direcciones") }) }
                    ElevatedCard { ListItem(headlineContent = { Text("Métodos de pago") }, supportingContent = { Text("Tarjetas y pagos") }) }
                }
            }
        }
    }
}