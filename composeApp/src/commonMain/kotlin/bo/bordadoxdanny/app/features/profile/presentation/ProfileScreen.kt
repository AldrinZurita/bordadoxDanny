package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.core.designsystem.components.dividers.HorizontalDivider
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.language
import bo.bordadoxdanny.app.logout
import bo.bordadoxdanny.app.profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsState()

    LaunchedEffect(state) {
        if (state is ProfileState.LoggedOut) {
            onNavigateToLogin()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.background)) {
        when (val currentState = state) {
            is ProfileState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is ProfileState.Success -> {
                ProfileContent(
                    state = currentState,
                    onIntent = viewModel::onIntent
                )
            }
            is ProfileState.Error -> {
                Text(
                    text = stringResource(currentState.messageResId),
                    color = AppTheme.colors.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {}
        }

        if (showLogoutDialog) {
            LogoutConfirmDialog(
                onConfirm = { viewModel.onIntent(ProfileIntent.OnLogoutConfirmed) },
                onDismiss = { viewModel.onIntent(ProfileIntent.OnLogoutDismissed) }
            )
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileState.Success,
    onIntent: (ProfileIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.profile),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        HorizontalDivider()

        // User Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(AppTheme.colors.primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AppIcon(
                            resource = AppIcons.Person,
                            contentDescription = null,
                            tint = AppTheme.colors.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "${state.user.firstName} ${state.user.lastName1}",
                            style = AppTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "@${state.user.username}",
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.textSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = state.user.email,
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textSecondary
                )
            }
        }

        // Language Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.language),
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LanguageDropdown(
                    selectedLanguage = state.availableLanguages.find { it.code == state.user.languageCode } ?: state.availableLanguages.first(),
                    languages = state.availableLanguages,
                    onLanguageSelected = { onIntent(ProfileIntent.OnLanguageSelected(it.code)) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout Button
        PrimaryButton(
            text = stringResource(Res.string.logout),
            onClick = { onIntent(ProfileIntent.OnLogoutClicked) },
            modifier = Modifier.fillMaxWidth(),
            containerColor = AppTheme.colors.error
        )
    }
}

@Composable
private fun LanguageDropdown(
    selectedLanguage: AppLanguage,
    languages: List<AppLanguage>,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.surface, RoundedCornerShape(8.dp))
                .border(1.dp, AppTheme.colors.divider, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIcon(resource = AppIcons.Language, contentDescription = null, modifier = Modifier.size(20.dp), tint = AppTheme.colors.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = selectedLanguage.displayName, style = AppTheme.typography.bodyMedium)
            }
            AppIcon(resource = AppIcons.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}
