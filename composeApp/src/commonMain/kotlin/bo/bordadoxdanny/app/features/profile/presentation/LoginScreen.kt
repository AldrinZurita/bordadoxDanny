package bo.bordadoxdanny.app.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToMain: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var emailOrUser by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Track if we've already navigated to prevent multiple calls
    val navigated = remember { mutableStateOf(false) }
    
    LaunchedEffect(state) {
        if (state is AuthState.LoginSuccess && !navigated.value) {
            navigated.value = true
            onNavigateToMain()
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
        LanguageSelector(modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(40.dp))

        AppIcon(
            resource = AppIcons.Lock,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = AppTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.welcome_back),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(Res.string.sign_in_subtitle),
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(Res.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) AppIcons.Visibility else AppIcons.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    AppIcon(resource = image, contentDescription = null)
                }
            },
            shape = RoundedCornerShape(8.dp),
            textStyle = AppTheme.typography.bodyMedium.copy(color = AppTheme.colors.textPrimary)
        )

        if (state is AuthState.LoginError) {
            Text(
                text = stringResource((state as AuthState.LoginError).messageResId),
                color = AppTheme.colors.error,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(Res.string.sign_in),
            onClick = { viewModel.onIntent(AuthIntent.OnLogin(emailOrUser, password)) },
            modifier = Modifier.fillMaxWidth(),
            isLoading = state is AuthState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.forgot_password),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.primary,
            modifier = Modifier.clickable { onNavigateToForgotPassword() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = stringResource(Res.string.no_account),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(Res.string.create_account),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .background(AppTheme.colors.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.demo_credentials),
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colors.textSecondary
            )
        }
    }
}

@Composable
fun LanguageSelector(modifier: Modifier = Modifier) {
    val viewModel: LanguageViewModel = koinViewModel()
    val languageState by viewModel.state.collectAsState()
    
    val languages = listOf(
        AppLanguage("en", "English"),
        AppLanguage("es", "Español"),
        AppLanguage("fr", "Français")
    )
    
    var expanded by remember { mutableStateOf(false) }
    val currentLanguageCode = when (languageState) {
        is LanguageState.Loaded -> (languageState as LanguageState.Loaded).languageCode
        else -> "en"
    }
    val currentLanguage = languages.find { it.code == currentLanguageCode } ?: languages.first()

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded = true }
        ) {
            AppIcon(resource = AppIcons.Language, contentDescription = null, modifier = Modifier.size(20.dp), tint = AppTheme.colors.primary)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = currentLanguage.displayName, style = AppTheme.typography.bodyMedium)
            AppIcon(resource = AppIcons.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang.displayName) },
                    onClick = { 
                        viewModel.onIntent(LanguageIntent.OnLanguageChanged(lang.code))
                        expanded = false 
                    }
                )
            }
        }
    }
}
