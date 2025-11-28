package com.example.farmacia_medicitas.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.farmacia_medicitas.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, vm: AuthViewModel = hiltViewModel()) {
    val emailOrUsername = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val state = vm.state

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Inicia sesión")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = emailOrUsername.value,
            onValueChange = { emailOrUsername.value = it },
            label = { Text("Email o usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { vm.login(emailOrUsername.value, password.value, onLoginSuccess) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Ingresar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        val s = state.value
        if (s.error != null) {
            Text(text = s.error!!, color = androidx.compose.ui.graphics.Color.Red)
        }
    }
}