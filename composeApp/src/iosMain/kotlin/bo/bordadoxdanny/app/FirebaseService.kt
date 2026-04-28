package bo.bordadoxdanny.app

actual class FirebaseService actual constructor() {
    /**
     * Implementación stub para iOS. 
     * Por ahora retorna un string vacío ya que FCM se maneja nativamente en iOS.
     */
    actual suspend fun getToken(): String {
        return ""
    }
}
