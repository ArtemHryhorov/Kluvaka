package co.kluvaka.cmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform