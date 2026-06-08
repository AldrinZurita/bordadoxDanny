package bo.bordadoxdanny.app.features.profile.data

import bo.bordadoxdanny.app.features.profile.domain.AuthRepository
import bo.bordadoxdanny.app.features.profile.domain.RegisterParams
import bo.bordadoxdanny.app.features.profile.domain.User
import bo.bordadoxdanny.app.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val firebaseManager: FirebaseManager
) : AuthRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override suspend fun login(emailOrUsername: String, password: String): Result<User> = runCatching {
        val cleanInput = emailOrUsername.trim()
        val cleanPassword = password.trim()

        // --- BYPASS DE CREDENCIALES DEMO ---
        val isDemoInput = cleanInput.equals("usuario@gmail.com", ignoreCase = true) || 
                          cleanInput.equals("usuario", ignoreCase = true)
        
        if (isDemoInput && cleanPassword == "Usuario123") {
            val demoUser = User(
                id = "demo-uid",
                username = "usuario",
                email = "usuario@gmail.com",
                phoneCountryCode = "+591",
                phoneNumber = "71234567",
                firstName = "Usuario",
                middleName = null,
                lastName1 = "Demo",
                lastName2 = null,
                ciNumber = "1234567",
                ciComplement = null,
                ciDepartment = "LP",
                languageCode = "en",
                syncStatus = "SYNCED"
            )
            
            try {
                userDao.upsert(demoUser.toEntity())
            } catch (e: Exception) {
                // Ignore Room errors for demo
            }
            
            return@runCatching demoUser
        }
        // -----------------------------------

        val email = if (cleanInput.contains("@")) {
            cleanInput.lowercase()
        } else {
            val usernameEmail = firebaseManager.getData("usernames/${cleanInput.lowercase()}", String::class).getOrThrow()
                ?: throw Exception("User not found")
            usernameEmail
        }

        val authResult = auth.signInWithEmailAndPassword(email, cleanPassword).await()
        val uid = authResult.user?.uid ?: throw Exception("Login failed")
        
        val userDto = firebaseManager.getData("users/$uid", UserDto::class).getOrThrow()
        if (userDto != null) {
            val entity = userDto.toEntity()
            userDao.upsert(entity)
            entity.toDomain()
        } else {
             userDao.getById(uid).firstOrNull()?.toDomain() 
                 ?: throw Exception("User data not found")
        }
    }

    override suspend fun register(params: RegisterParams): Result<User> = runCatching {
        val emailClean = params.email.trim().lowercase()
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
            syncStatus = "PENDING"
        )
        
        userDao.upsert(user.toEntity())
        firebaseManager.saveData("usernames/${user.username.lowercase()}", user.email).getOrThrow()
        firebaseManager.saveData("users/$uid", user.toEntity().toDto()).getOrThrow()
        
        user
    }

    override suspend fun sendVerificationCode(emailOrUsername: String): Result<String> = runCatching {
        val cleanInput = emailOrUsername.trim()
        val email = if (cleanInput.contains("@")) cleanInput.lowercase() else {
             firebaseManager.getData("usernames/${cleanInput.lowercase()}", String::class).getOrThrow()
                 ?: throw Exception("User not found")
        }
        auth.sendPasswordResetEmail(email).await()
        email
    }

    override suspend fun verifyCode(email: String, code: String): Result<Unit> = Result.success(Unit)

    override suspend fun resetPassword(email: String, newPassword: String): Result<Unit> = Result.success(Unit)

    override suspend fun logout(): Result<Unit> = runCatching {
        auth.signOut()
        userDao.deleteAll()
    }

    override fun getCurrentUser(): Flow<User?> {
        val uid = auth.currentUser?.uid ?: ""
        return userDao.getById(uid).map { it?.toDomain() }
    }

    override suspend fun updateLanguage(userId: String, languageCode: String): Result<Unit> = runCatching {
        val userEntity = userDao.getById(userId).firstOrNull() ?: throw Exception("User not found")
        val updatedUser = userEntity.copy(languageCode = languageCode, syncStatus = "PENDING")
        userDao.upsert(updatedUser)
    }
}
