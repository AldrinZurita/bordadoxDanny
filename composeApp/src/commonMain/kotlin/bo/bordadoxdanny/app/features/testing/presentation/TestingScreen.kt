package bo.bordadoxdanny.app.features.testing.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.firebase.FirebaseManager
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import bo.bordadoxdanny.app.workers.WorkerScheduler
import bo.bordadoxdanny.app.features.profile.domain.ProfileRepository
import bo.bordadoxdanny.app.features.profile.domain.Profile
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.welcome_message
import bo.bordadoxdanny.app.sync_data_label
import bo.bordadoxdanny.app.firebase_status_label
import bo.bordadoxdanny.app.firebase_status_pending
import bo.bordadoxdanny.app.firebase_status_saving
import bo.bordadoxdanny.app.firebase_status_success
import bo.bordadoxdanny.app.firebase_status_failed
import bo.bordadoxdanny.app.test_firebase_button

@Composable
fun TestingScreen(
    onNavigateToDaemon: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val firebaseManager = remember { FirebaseManager() }
    
    val logScheduler = koinInject<WorkerScheduler>()
    val profileRepository = koinInject<ProfileRepository>()
    
    var statusType by remember { mutableStateOf("pending") }
    var errorMessage by remember { mutableStateOf("") }
    var roomMessage by remember { mutableStateOf("Esperando acción de Room...") }

    val statusText = when (statusType) {
        "pending" -> stringResource(Res.string.firebase_status_pending)
        "saving" -> stringResource(Res.string.firebase_status_saving)
        "success" -> stringResource(Res.string.firebase_status_success)
        "failed" -> stringResource(Res.string.firebase_status_failed, errorMessage)
        else -> ""
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.welcome_message),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Go to Daemon Status",
            onClick = onNavigateToDaemon
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(Res.string.test_firebase_button),
            isLoading = statusType == "saving",
            onClick = {
                scope.launch {
                    statusType = "saving"
                    val currentTime = Clock.System.now().toEpochMilliseconds()
                    val result = firebaseManager.saveData("test_connection", "Conectado a las $currentTime")
                    
                    if (result.isSuccess) {
                        statusType = "success"
                    } else {
                        errorMessage = result.exceptionOrNull()?.message ?: "Error"
                        statusType = "failed"
                    }
                }
            }
        )
        
        Text(
            text = stringResource(Res.string.firebase_status_label, statusText),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Probar Room (Guardar Perfil)",
            onClick = {
                scope.launch {
                    try {
                        roomMessage = "Guardando en Room..."
                        val testProfile = Profile(
                            id = 1, 
                            name = "Test Room User", 
                            email = "room@test.com", 
                            phone = "999999"
                        )
                        profileRepository.updateProfile(testProfile)
                        roomMessage = "✅ Éxito: Perfil guardado en Room!"
                    } catch (e: Exception) {
                        roomMessage = "❌ Error Room: ${e.message}"
                    }
                }
            }
        )
        Text(
            text = roomMessage,
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(Res.string.sync_data_label),
            onClick = {
                logScheduler.testWorkImmediately()
            }
        )
    }
}
