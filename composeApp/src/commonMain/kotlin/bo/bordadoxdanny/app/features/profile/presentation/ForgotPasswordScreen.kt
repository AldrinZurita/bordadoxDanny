package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.components.inputs.BasicInput
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToVerification: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var emailOrUser by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is AuthState.CodeSent) {
            onNavigateToVerification((state as AuthState.CodeSent).email)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { onNavigateBack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Using ExpandMore as a placeholder for ArrowBack if not available
                AppIcon(
                    resource = AppIcons.ExpandMore, 
                    contentDescription = null, 
                    modifier = Modifier.size(20.dp), 
                    tint = AppTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.back_to_login), 
                    color = AppTheme.colors.primary, 
                    style = AppTheme.typography.bodyMedium
                )
            }
            LanguageSelector()
        }

        Spacer(modifier = Modifier.height(60.dp))

        AppIcon(
            resource = AppIcons.Lock,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = AppTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.reset_password),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.reset_password_subtitle),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        BasicInput(
            value = emailOrUser,
            onValueChange = { emailOrUser = it },
            label = stringResource(Res.string.email_or_username),
            modifier = Modifier.fillMaxWidth()
        )

        if (state is AuthState.CodeSentError) {
            Text(
                text = stringResource((state as AuthState.CodeSentError).messageResId),
                color = AppTheme.colors.error,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(Res.string.send_verification_code),
            onClick = { viewModel.onIntent(AuthIntent.OnSendCode(emailOrUser)) },
            modifier = Modifier.fillMaxWidth(),
            isLoading = state is AuthState.Loading
        )
    }
}
