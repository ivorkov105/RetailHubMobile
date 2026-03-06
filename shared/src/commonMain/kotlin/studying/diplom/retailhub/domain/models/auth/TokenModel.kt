package studying.diplom.retailhub.domain.models.auth

data class TokenModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int
)