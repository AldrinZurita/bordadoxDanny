package bo.bordadoxdanny.app.features.config.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = koinViewModel()
) {
    val configs by viewModel.configs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Configuración Remota (Caché Local)",
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (configs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(configs.toList()) { (key, value) ->
                    ConfigItem(key, value)
                }
            }
        }
    }
}

@Composable
fun ConfigItem(key: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = key,
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colors.textPrimary
            )
            Text(
                text = value,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textPrimary
            )
        }
    }
}
