package com.ntp.notengoplan


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Estado para almacenar usuarios
            var usuarios by remember { mutableStateOf<List<usuarios>>(emptyList()) }

            // Llamar a la API
            LaunchedEffect(Unit) {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                api.getUserS().enqueue(object : Callback<List<usuarios>> {
                    override fun onResponse(
                        call: Call<List<usuarios>>,
                        response: Response<List<usuarios>>
                    ) {
                        if (response.isSuccessful) {
                            usuarios = response.body() ?: emptyList()
                        } else {
                            Log.e("API", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<usuarios>>, t: Throwable) {
                        Log.e("API", "Fallo en la solicitud: ${t.message}")
                    }
                })
            }

            // Renderizar la interfaz
            UsuarioListScreen(usuarios)
        }
    }
}

@Composable
fun UsuarioListScreen(usuarios: List<usuarios>) {
    // Pantalla principal que muestra una lista de usuarios
    MaterialTheme {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(usuarios) { usuario ->
                UsuarioCard(usuario)
            }
        }
    }
}

@Composable
fun UsuarioCard(usuario: usuarios) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${usuario.id}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Nombre: ${if (usuario.nombre.isNotBlank()) usuario.nombre else "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Email: ${usuario.email}", style = MaterialTheme.typography.bodyMedium)
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




