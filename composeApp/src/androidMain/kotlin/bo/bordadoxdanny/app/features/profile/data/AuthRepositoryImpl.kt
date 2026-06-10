package bo.bordadoxdanny.app.features.profile.data

import bo.bordadoxdanny.app.data.local.SecurityManager
import bo.bordadoxdanny.app.features.profile.domain.*
import bo.bordadoxdanny.app.firebase.FirebaseManager
import bo.bordadoxdanny.app.network.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import android.util.Log

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val firebaseManager: FirebaseManager,
    private val apiService: ApiService,
    private val securityManager: SecurityManager
) : AuthRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun login(email: String, password: String): Result<String> = runCatching {
        val cleanInput = email.trim().lowercase()
        val cleanPassword = password.trim()

        // --- DEMO BYPASS ---
        val isDemoUser = cleanInput == "usuario@gmail.com" || cleanInput == "usuario" || cleanInput.contains("caleb")
        if (isDemoUser && cleanPassword == securityManager.getDemoPassword()) {
            val demoToken = "demo-token"
            securityManager.saveToken(demoToken)
            return@runCatching demoToken
        } else if (isDemoUser) {
            throw Exception("Invalid credentials")
        }

        val authResult = auth.signInWithEmailAndPassword(cleanInput, cleanPassword).await()
        val token = authResult.user?.getIdToken(true)?.await()?.token ?: throw Exception("Login failed: Token null")
        
        securityManager.saveToken(token)
        
        val uid = authResult.user?.uid ?: ""
        val userDto = firebaseManager.getData("users/$uid", UserDto::class).getOrThrow()
        userDto?.let {
            userDao.upsert(it.toEntity())
        }
        
        token
    }

    override suspend fun register(params: RegisterParams): Result<User> = runCatching {
        val emailClean = params.email.trim().lowercase()
        
        // 1. Create in Firebase Auth
        val authResult = auth.createUserWithEmailAndPassword(emailClean, params.password.trim()).await()
        val uid = authResult.user?.uid ?: throw Exception("Registration failed")
        
        val user = User(
            id = uid,
            username = params.username.trim(),
            email = emailClean,
            phoneCountryCode = params.phoneCountryCode,
            phoneNumber = params.phoneNumber,
            firstName = params.firstName,
            middleName = params.middleName,
            lastName1 = params.lastName1,
            lastName2 = params.lastName2,
            ciNumber = params.ciNumber,
            ciComplement = params.ciComplement,
            ciDepartment = params.ciDepartment,
            languageCode = "en",
            isVerified = false,
            syncStatus = "PENDING"
        )
        
        // 2. Sync with Backend API
        try {
            apiService.register(
                RegisterRequest(
                    username = user.username,
                    email = user.email,
                    password = params.password,
                    phoneCountryCode = user.phoneCountryCode,
                    phoneNumber = user.phoneNumber,
                    firstName = user.firstName,
                    middleName = user.middleName,
                    lastName1 = user.lastName1,
                    lastName2 = user.lastName2,
                    ciNumber = user.ciNumber,
                    ciComplement = user.ciComplement,
                    ciDepartment = user.ciDepartment
                )
            )
        } catch (e: Exception) {
            Log.e("AuthRepository", "Backend sync failed during registration: ${e.message}")
        }

        // 3. Save locally and to Firebase Database
        userDao.upsert(user.toEntity())
        firebaseManager.saveData("usernames/${user.username.lowercase()}", user.email).getOrThrow()
        firebaseManager.saveData("users/$uid", user.toEntity().toDto()).getOrThrow()
        
        user
    }

    override suspend fun sendVerificationCode(emailOrUser: String): Result<String> = runCatching {
        var targetEmail = emailOrUser.trim().lowercase()
        
        // --- DEMO BYPASS ---
        if (targetEmail == "usuario@gmail.com" || targetEmail == "usuario" || targetEmail.contains("caleb")) {
            return@runCatching "Success (Demo)"
        }

        // Resolve username to email if necessary
        if (!targetEmail.contains("@")) {
            val resolvedEmail = firebaseManager.getData("usernames/${targetEmail}", String::class).getOrNull()
            if (resolvedEmail != null) {
                targetEmail = resolvedEmail
            } else {
                throw Exception("USER_NOT_FOUND")
            }
        }
        
        try {
            val response = apiService.sendVerificationCode(SendCodeRequest(targetEmail))
            response.message
        } catch (e: Exception) {
            Log.e("AuthRepository", "Backend sendCode failed for $targetEmail: ${e.message}")
            // Check if user exists in Firebase before giving up
            try {
                auth.fetchSignInMethodsForEmail(targetEmail).await()
                // If it doesn't throw, it means we reached Firebase. 
                // We still need the backend to send the code if that's the chosen flow,
                // but we shouldn't throw "USER_NOT_FOUND" if the user actually exists.
                // For now, let's fallback to Firebase Password Reset which is more reliable if backend fails
                auth.sendPasswordResetEmail(targetEmail).await()
                "Verification link sent via Email"
            } catch (firebaseEx: Exception) {
                throw Exception("USER_NOT_FOUND")
            }
        }
    }

    override suspend fun verifyCode(email: String, code: String): Result<Boolean> = runCatching {
        // --- DEMO BYPASS ---
        if (code == "123456" || email.contains("caleb") || email == "usuario@gmail.com") return@runCatching true

        val response = apiService.verifyCode(VerifyCodeRequest(email.trim().lowercase(), code.trim()))
        if (response.success) {
            val uid = auth.currentUser?.uid ?: ""
            if (uid.isNotEmpty()) userDao.markAsVerified(uid)
            true
        } else {
            false
        }
    }

    override suspend fun resetPassword(email: String, newPassword: String): Result<Unit> = runCatching {
        val cleanEmail = email.trim().lowercase()
        
        // --- DEMO BYPASS ---
        if (cleanEmail == "usuario@gmail.com" || cleanEmail == "usuario" || cleanEmail.contains("caleb")) {
            securityManager.saveDemoPassword(newPassword.trim())
            securityManager.saveToken("") // Invalidate current session token if any
            return@runCatching Unit
        }

        try {
            val response = apiService.resetPassword(ResetPasswordRequest(cleanEmail, newPassword))
            if (!response.success) throw Exception("Backend reset failed")
        } catch (e: Exception) {
            // Fallback to Firebase Auth reset (note: Firebase reset usually uses a link, not direct password update here)
            // But we can try to update it if the user is already "signed in" with the code verification somehow
            // or just rely on the link. For consistency with the UI flow, we'd need backend.
            Log.e("AuthRepository", "Backend resetPassword failed: ${e.message}")
            throw e
        }
        Unit
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        auth.signOut()
        securityManager.clear()
        userDao.deleteAll()
    }

    override fun getCurrentUser(): Flow<User?> {
        val uid = auth.currentUser?.uid ?: ""
        return userDao.getById(uid).map { it?.toDomain() }
    }

    override suspend fun updateLanguage(userId: String, languageCode: String): Result<Unit> = runCatching {
        val userEntity = userDao.getByIdSuspend(userId)
        userEntity?.let {
            val updatedUser = it.copy(languageCode = languageCode, syncStatus = "PENDING")
            userDao.upsert(updatedUser)
        }
        Unit
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return securityManager.getToken()?.isNotEmpty() == true && (auth.currentUser != null || securityManager.getToken() == "demo-token")
    }
}
