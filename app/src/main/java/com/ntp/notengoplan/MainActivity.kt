package com.ntp.notengoplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura el contenido con Jetpack Compose
        setContent {
            MyComposeApp()
        }
    }
}

@Composable
fun MyComposeApp() {
    MaterialTheme {
        // Agrega un contenedor principal para organizar los componentes
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MyMaterialButton()
            MyTextField()
            MyCard()
        }
    }
}

@Composable
fun MyMaterialButton() {
    Button(
        onClick = { /* Acción al hacer clic */ },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Click Me", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun MyTextField() {
    TextField(
        value = "Hello",
        onValueChange = {},
        label = { Text("Enter text") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text("This is a card", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
