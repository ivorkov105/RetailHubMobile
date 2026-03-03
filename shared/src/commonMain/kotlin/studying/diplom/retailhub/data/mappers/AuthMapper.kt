package studying.diplom.retailhub.data.mappers

import studying.diplom.retailhub.data.enteties.auth.TokenEntity
import studying.diplom.retailhub.domain.models.TokenModel

fun TokenEntity.toModel(): TokenModel = TokenModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    expiresIn = expiresIn
)
