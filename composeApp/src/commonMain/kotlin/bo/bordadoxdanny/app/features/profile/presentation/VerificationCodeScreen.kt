package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.components.inputs.BasicInput
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun VerificationCodeScreen(
    email: String,
    viewModel: AuthViewModel,
    onNavigateToNewPassword: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var code by remember { mutableStateOf("") }
    var seconds by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) {
        while (seconds > 0) {
            delay(1000)
            seconds--
        }
    }

    LaunchedEffect(state) {
        if (state is AuthState.CodeVerified) {
            onNavigateToNewPassword((state as AuthState.CodeVerified).email)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        AppIcon(
            resource = AppIcons.Shield,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = AppTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.verification_code),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.code_sent_to, email),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.verification_code),
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BasicInput(
                value = code,
                onValueChange = { 
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        code = it
                    }
                },
                label = "123456",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        if (state is AuthState.CodeVerifiedError) {
            Text(
                text = stringResource((state as AuthState.CodeVerifiedError).messageResId),
                color = AppTheme.colors.error,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(Res.string.verify_code),
            onClick = { viewModel.onIntent(AuthIntent.OnVerifyCode(email, code)) },
            modifier = Modifier.fillMaxWidth(),
            isLoading = state is AuthState.Loading,
            enabled = code.length == 6
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (seconds > 0) {
            Text(
                text = stringResource(Res.string.resend_in, seconds),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary
            )
        } else {
            Text(
                text = stringResource(Res.string.resend_code),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { 
                    viewModel.onIntent(AuthIntent.OnSendCode(email))
                    seconds = 30
                }
            )
        }
    }
}
