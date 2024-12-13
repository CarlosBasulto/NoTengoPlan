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
import usuarios

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
           // UsuarioListScreen(usuarios)

            // Renderizar la interfaz
          /*  UsuarioListScreen(usuarios) { usuarioId ->
                // Eliminar usuario de la lista y llamar a la API
                usuarios = usuarios.filter { it.id != usuarioId }
                eliminarUsuarioEnServidor(usuarioId)
            }
*/



            UsuarioListScreen(
                usuarios = usuarios,
                onDeleteUser = { usuarioId ->
                    usuarios = usuarios.filter { it.id != usuarioId }
                    eliminarUsuarioEnServidor(usuarioId)
                },
                onEditUser = { usuario ->
                    editarUsuarioEnServidor(usuario) // Implementa tu lógica
                },
                onAddUser = { nuevoUsuario ->
                    usuarios = usuarios + nuevoUsuario
                    añadirUsuarioEnServidor(nuevoUsuario) // Implementa tu lógica
                }
            )


        }
    }
    private fun editarUsuarioEnServidor(usuario: usuarios) {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        api.updateUser(usuario.id.toInt(), usuario) // Se envía el objeto completo
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("API", "Usuario editado con éxito")
                    } else {
                        Log.e("API", "Error al editar usuario: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API", "Fallo al editar usuario: ${t.message}")
                }
            })
    }

    private fun añadirUsuarioEnServidor(usuario: usuarios) {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        api.addUser(usuario) // Se envía el objeto completo
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("API", "Usuario añadido con éxito")
                    } else {
                        Log.e("API", "Error al añadir usuario: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API", "Fallo al añadir usuario: ${t.message}")
                }
            })
    }

    private fun eliminarUsuarioEnServidor(usuarioId: Int) {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        api.delUser(usuarioId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API", "Usuario eliminado con éxito")
                } else {
                    Log.e("API", "Error al eliminar usuario: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Fallo en la solicitud de eliminación: ${t.message}")
            }
        })
    }
}
/*
@Composable
fun UsuarioListScreen(usuarios: List<usuarios>, onDeleteUser: (Int) -> Unit) {
    MaterialTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(usuarios) { usuario ->
                UsuarioCard(usuario, onDeleteUser)
            }
        }
    }
}*/

@Composable
fun UsuarioListScreen(
    usuarios: List<usuarios>,
    onDeleteUser: (Int) -> Unit,
    onEditUser: (usuarios) -> Unit,
    onAddUser: (usuarios) -> Unit
) {
    // Estado para mostrar el formulario de añadir/editar
    var showForm by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<usuarios?>(null) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = {
                    editingUser = null // Restablece para añadir un nuevo usuario
                    showForm = true
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Añadir Usuario")
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(usuarios) { usuario ->
                    UsuarioCard(
                        usuario = usuario,
                        onDeleteUser = onDeleteUser,
                        onEditUser = {
                            editingUser = it // Carga el usuario en edición
                            showForm = true
                        }
                    )
                }
            }
        }
    }

    if (showForm) {
        UsuarioForm(
            usuario = editingUser,
            onDismiss = { showForm = false },
            onSubmit = { nuevoUsuario ->
                if (editingUser == null) {
                    onAddUser(nuevoUsuario)
                } else {
                    onEditUser(nuevoUsuario)
                }
                showForm = false
            }
        )
    }
}

@Composable
fun UsuarioForm(
    usuario: usuarios?,
    onDismiss: () -> Unit,
    onSubmit: (usuarios) -> Unit
) {
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var email by remember { mutableStateOf(usuario?.email ?: "") }
    var password by remember { mutableStateOf(usuario?.password ?: "") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(if (usuario == null) "Añadir Usuario" else "Editar Usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(
                        usuario?.clone(
                            nombre = nombre,
                            email = email,
                            password = password
                        ) ?: usuarios().clone(
                            nombre = nombre,
                            email = email,
                            password = password
                        )
                    )
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}


/*
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
*/

/*
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
}*/


@Composable
fun UsuarioCard(
    usuario: usuarios,
    onDeleteUser: (Int) -> Unit,
    onEditUser: (usuarios) -> Unit
) {
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { onEditUser(usuario) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = { onDeleteUser(usuario.id.toInt()) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Borrar")
                }
            }
        }
    }
}


/*
@Composable
fun UsuarioCard(usuario: usuarios, onDeleteUser: (Int) -> Unit) {
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

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de borrar
            Button(
                onClick = { onDeleteUser(usuario.id.toInt()) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Borrar", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}

*/
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




