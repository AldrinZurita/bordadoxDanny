package bo.bordadoxdanny.app


expect class FirebaseService() {
    /**
     * Obtiene el token de FCM de forma asíncrona.
     */
    suspend fun getToken(): String
}