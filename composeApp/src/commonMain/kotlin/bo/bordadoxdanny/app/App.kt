package bo.bordadoxdanny.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.theme.DsTheme
import bo.bordadoxdanny.app.core.designsystem.theme.ThemeMode
import bo.bordadoxdanny.app.features.navigation.MainNavigationContainer

@Composable
fun App(
    onResetOnboarding: () -> Unit = {}
) {
    var currentMode by remember { mutableStateOf(ThemeMode.LIGHT) }
    val snackbarHostState = remember { SnackbarHostState() }

    DsTheme(mode = currentMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colors.background
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = AppTheme.colors.background
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    // Theme switcher row (Esto se mantiene igual)
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeMode.entries.forEach { mode ->
                            PrimaryButton(
                                text = mode.name,
                                onClick = { currentMode = mode },
                                enabled = currentMode != mode
                            )
                        }
                    }
                    
                    // Main Content
                    // Cambiamos AppNavHost por MainNavigationContainer
                    Box(modifier = Modifier.weight(1f)) {
                        MainNavigationContainer(
                            onResetOnboarding = onResetOnboarding
                        )
                    }
                }
            }
        }
    }
}
