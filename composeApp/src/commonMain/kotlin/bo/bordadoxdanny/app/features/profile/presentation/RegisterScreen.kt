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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import bo.bordadoxdanny.app.Res
import bo.bordadoxdanny.app.*
import bo.bordadoxdanny.app.core.designsystem.components.buttons.PrimaryButton
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcon
import bo.bordadoxdanny.app.core.designsystem.components.icons.AppIcons
import bo.bordadoxdanny.app.core.designsystem.components.inputs.BasicInput
import bo.bordadoxdanny.app.core.designsystem.theme.AppTheme
import bo.bordadoxdanny.app.features.profile.domain.RegisterParams
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val requirements by viewModel.passwordRequirements.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("BO +591") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var firstName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var lastName1 by remember { mutableStateOf("") }
    var lastName2 by remember { mutableStateOf("") }
    var ciNumber by remember { mutableStateOf("") }
    var ciComplement by remember { mutableStateOf("") }
    var ciDepartment by remember { mutableStateOf("LP") }

    // Track if we've already navigated to prevent multiple calls
    val navigated = remember { mutableStateOf(false) }
    
    LaunchedEffect(state) {
        if (state is AuthState.RegisterSuccess && !navigated.value) {
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
        LanguageSelector(modifier = Modifier.align(Alignment.End))

        Text(
            text = stringResource(Res.string.create_account_title),
            style = AppTheme.typography.headlineLarge,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = stringResource(Res.string.join_today),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.textSecondary,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Phone Number Row
        Row(modifier = Modifier.fillMaxWidth()) {
            CountryCodeDropdown(
                selected = countryCode,
                onSelected = { countryCode = it },
                modifier = Modifier.width(120.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicInput(
                value = phoneNumber,
                onValueChange = { if (it.all { char -> char.isDigit() }) phoneNumber = it },
                label = stringResource(Res.string.phone_number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicInput(
            value = username,
            onValueChange = { username = it },
            label = stringResource(Res.string.username),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicInput(
            value = email,
            onValueChange = { email = it },
            label = stringResource(Res.string.email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                viewModel.onIntent(AuthIntent.OnPasswordChanged(it))
            },
            label = { Text(stringResource(Res.string.password)) },
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            BasicInput(
                value = firstName,
                onValueChange = { firstName = it },
                label = stringResource(Res.string.first_name),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicInput(
                value = middleName,
                onValueChange = { middleName = it },
                label = stringResource(Res.string.middle_name) + " (" + stringResource(Res.string.optional_field) + ")",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            BasicInput(
                value = lastName1,
                onValueChange = { lastName1 = it },
                label = stringResource(Res.string.last_name_1),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicInput(
                value = lastName2,
                onValueChange = { lastName2 = it },
                label = stringResource(Res.string.last_name_2) + " (" + stringResource(Res.string.optional_field) + ")",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            BasicInput(
                value = ciNumber,
                onValueChange = { if (it.all { char -> char.isDigit() }) ciNumber = it },
                label = stringResource(Res.string.ci_number),
                modifier = Modifier.weight(0.4f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicInput(
                value = ciComplement,
                onValueChange = { if (it.length <= 3) ciComplement = it },
                label = stringResource(Res.string.ci_complement),
                modifier = Modifier.weight(0.25f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            CIDepartmentDropdown(
                selected = ciDepartment,
                onSelected = { ciDepartment = it },
                modifier = Modifier.weight(0.35f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(Res.string.create_account),
            onClick = {
                viewModel.onIntent(AuthIntent.OnRegister(
                    RegisterParams(
                        username = username,
                        email = email,
                        password = password,
                        phoneCountryCode = countryCode,
                        phoneNumber = phoneNumber,
                        firstName = firstName,
                        middleName = middleName.takeIf { it.isNotBlank() },
                        lastName1 = lastName1,
                        lastName2 = lastName2.takeIf { it.isNotBlank() },
                        ciNumber = ciNumber,
                        ciComplement = ciComplement.takeIf { it.isNotBlank() },
                        ciDepartment = ciDepartment
                    )
                ))
            },
            modifier = Modifier.fillMaxWidth(),
            isLoading = state is AuthState.Loading,
            enabled = requirements.allMet && username.isNotBlank() && email.isNotBlank() && firstName.isNotBlank() && lastName1.isNotBlank() && ciNumber.isNotBlank()
        )

        if (state is AuthState.RegisterError) {
            Text(
                text = stringResource((state as AuthState.RegisterError).messageResId),
                color = AppTheme.colors.error,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = stringResource(Res.string.already_have_account),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.textSecondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(Res.string.sign_in),
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToLogin() }
            )
        }
    }
}

@Composable
private fun CountryCodeDropdown(selected: String, onSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    val codes = listOf("BO +591", "AR +54", "BR +55", "CL +56", "PE +51")
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            trailingIcon = { AppIcon(resource = AppIcons.ExpandMore, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = AppTheme.colors.textPrimary,
                disabledBorderColor = AppTheme.colors.textPrimary.copy(alpha = 0.5f)
            )
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            codes.forEach { code ->
                DropdownMenuItem(text = { Text(code) }, onClick = { onSelected(code); expanded = false })
            }
        }
    }
}

@Composable
private fun CIDepartmentDropdown(selected: String, onSelected: (String) -> Unit, modifier: Modifier = Modifier) {
    val depts = listOf("LP", "OR", "CB", "PT", "TJ", "SC", "BE", "PA", "CH")
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            trailingIcon = { AppIcon(resource = AppIcons.ExpandMore, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = AppTheme.colors.textPrimary,
                disabledBorderColor = AppTheme.colors.textPrimary.copy(alpha = 0.5f)
            )
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            depts.forEach { dept ->
                DropdownMenuItem(text = { Text(dept) }, onClick = { onSelected(dept); expanded = false })
            }
        }
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
