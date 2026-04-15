package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val profile by viewModel.profile.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Perfil", style = MaterialTheme.typography.headlineMedium)
        profile?.let {
            Text("Nombre: ${it.name}", modifier = Modifier.padding(top = 8.dp))
            Text("Email: ${it.email}")
            Text("Teléfono: ${it.phone}")
        } ?: Text("Cargando perfil...")
    }
}
