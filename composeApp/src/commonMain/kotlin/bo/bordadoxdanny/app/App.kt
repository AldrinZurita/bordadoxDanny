package bo.bordadoxdanny.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.core.designsystem.theme.DsTheme
import bo.bordadoxdanny.app.core.designsystem.theme.ThemeMode
import bo.bordadoxdanny.app.features.navigation.AppNavHost

@Composable
fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    
    DsTheme(mode = ThemeMode.LIGHT) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colors.background
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = AppTheme.colors.background
            ) { _ ->
                AppNavHost()
            }
        }
    }
}
