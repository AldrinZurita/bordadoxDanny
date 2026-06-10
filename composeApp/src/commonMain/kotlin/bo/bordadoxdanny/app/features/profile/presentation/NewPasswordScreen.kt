package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun NewPasswordScreen(
    email: String,
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val requirements by viewModel.passwordRequirements.collectAsState()
    
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is AuthState.PasswordResetSuccess) {
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        AppIcon(
            resource = AppIcons.Lock,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = AppTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.new_password),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.new_password_subtitle),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // New Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                viewModel.onIntent(AuthIntent.OnPasswordChanged(it))
            },
            label = { Text(stringResource(Res.string.new_password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) AppIcons.Visibility else AppIcons.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    AppIcon(resource = image, contentDescription = null)
                }
            },
            leadingIcon = {
                AppIcon(resource = AppIcons.Lock, contentDescription = null, tint = AppTheme.colors.textSecondary)
            },
            shape = RoundedCornerShape(8.dp),
            textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                viewModel.onIntent(AuthIntent.OnConfirmPasswordChanged(it))
            },
            label = { Text(stringResource(Res.string.confirm_password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) AppIcons.Visibility else AppIcons.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    AppIcon(resource = image, contentDescription = null)
                }
            },
            leadingIcon = {
                AppIcon(resource = AppIcons.Lock, contentDescription = null, tint = AppTheme.colors.textSecondary)
            },
            shape = RoundedCornerShape(8.dp),
            textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Requirements Block
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.primary.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RequirementItem(stringResource(Res.string.password_req_lowercase), requirements.hasLowercase)
            RequirementItem(stringResource(Res.string.password_req_uppercase), requirements.hasUppercase)
            RequirementItem(stringResource(Res.string.password_req_number), requirements.hasNumber)
            RequirementItem(stringResource(Res.string.password_req_length), requirements.hasMinLength)
        }

        if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            Text(
                text = stringResource(Res.string.passwords_do_not_match),
                color = AppTheme.colors.error,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (state is AuthState.PasswordResetError) {
            Text(
                text = stringResource((state as AuthState.PasswordResetError).messageResId),
                color = AppTheme.colors.error,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(Res.string.reset_password_action),
            onClick = { viewModel.onIntent(AuthIntent.OnResetPassword(email, password)) },
            modifier = Modifier.fillMaxWidth(),
            isLoading = state is AuthState.Loading,
            enabled = requirements.allMet && password == confirmPassword
        )
    }
}

@Composable
private fun RequirementItem(text: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val icon = if (isMet) AppIcons.Check else AppIcons.Cancel
        val tint = if (isMet) Color(0xFF4CAF50) else AppTheme.colors.error
        AppIcon(resource = icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = tint)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.textPrimary)
    }
}
