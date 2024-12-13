package com.ntp.notengoplan

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape

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
            AssistChipExample()
            VerticalDividerExample()
            RoundButtonExample(onClick = {
                // Acción al hacer clic en el botón
                println("Botón redondo presionado")
            })
        }
    }
}
@Composable
fun VerticalDividerExample() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("First item in row")
        VerticalDivider(color = MaterialTheme.colorScheme.secondary)
        Text("Second item in row")
    }
}

@Composable
fun AssistChipExample() {
    AssistChip(
        onClick = { Log.d("Assist chip", "hello world") },
        label = { Text("Assist chip") },
        leadingIcon = {
          //  Icon(
               // Icons.Filled.Settings,
               // contentDescription = "Localized description",
                //Modifier.size(AssistChipDefaults.IconSize)
          //  )
        }
    )
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
fun RoundButtonExample(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        shape = CircleShape, // Forma redonda completa
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.size(56.dp) // Asegura que sea cuadrado para mantener la forma redonda
    ) {
        Text(
            text = "OK",
           // color ="",
            style = MaterialTheme.typography.bodyMedium
        )
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




