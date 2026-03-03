package studying.diplom.retailhub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform