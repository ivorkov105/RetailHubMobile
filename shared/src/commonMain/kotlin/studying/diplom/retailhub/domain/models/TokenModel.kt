package studying.diplom.retailhub.domain.models

data class TokenModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int
)
