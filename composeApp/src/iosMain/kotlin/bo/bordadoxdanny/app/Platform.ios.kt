package bo.bordadoxdanny.app

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual suspend fun getFirebaseToken(): String? {
    // Firebase Messaging not implemented for iOS yet in this guide
    return null
}
