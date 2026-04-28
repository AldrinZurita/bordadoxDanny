package bo.bordadoxdanny.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect suspend fun getFirebaseToken(): String?
