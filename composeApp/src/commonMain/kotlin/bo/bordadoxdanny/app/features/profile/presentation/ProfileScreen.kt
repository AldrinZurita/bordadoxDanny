package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.a11y_profile_name
import bo.bordadoxdanny.app.a11y_profile_email
import bo.bordadoxdanny.app.a11y_profile_phone
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// TalkBack announces: "Perfil, heading"
// Each profile field is read with its label
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val profile by viewModel.profile.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Perfil", 
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )
        profile?.let {
            val nameDesc = stringResource(Res.string.a11y_profile_name, it.name)
            val emailDesc = stringResource(Res.string.a11y_profile_email, it.email)
            val phoneDesc = stringResource(Res.string.a11y_profile_phone, it.phone)

            Text(
                text = "Nombre: ${it.name}", 
                modifier = Modifier.padding(top = 8.dp).semantics {
                    contentDescription = nameDesc
                },
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary
            )
            Text(
                text = "Email: ${it.email}",
                modifier = Modifier.semantics {
                    contentDescription = emailDesc
                },
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary
            )
            Text(
                text = "Teléfono: ${it.phone}",
                modifier = Modifier.semantics {
                    contentDescription = phoneDesc
                },
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary
            )
        } ?: Text(
            text = "Cargando perfil...",
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textPrimary
        )
    }
}
