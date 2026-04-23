package bo.bordadoxdanny.app.features.settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen() {
    val viewModel: SettingsViewModel = koinViewModel()
    val prefs by viewModel.preferences.collectAsState()
    
    val supportedLanguages = listOf(
        "en-US" to "English (USA)",
        "es-ES" to "Spanish (Spain)",
        "fr-FR" to "French (France)",
        "de-DE" to "German (Germany)",
        "pt-PT" to "Portuguese (Portugal)"
    )
    
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Notification Settings", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            val currentName = supportedLanguages.find { it.first == prefs?.languageCode }?.second ?: "English (USA)"
            
            OutlinedTextField(
                value = currentName,
                onValueChange = {},
                readOnly = true,
                label = { Text("App Language") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                supportedLanguages.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            viewModel.updateLanguage(code)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { viewModel.triggerTest() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simulate Translated Push")
        }
        
        Text(
            text = "Note: If no notification appears, ensure 'Post Notifications' permission is enabled in App Settings.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
