package bo.bordadoxdanny.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform