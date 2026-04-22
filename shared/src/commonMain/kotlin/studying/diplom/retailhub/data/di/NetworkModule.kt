package studying.diplom.retailhub.data.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.domain.repositories.AuthRepository

val networkModule = module {
    single {
        val localSource = get<LocalSource>()
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
            coerceInputValues = true
        }

        HttpClient {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(json)
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor: $message")
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val session = localSource.getSession()
                        if (session != null) {
                            BearerTokens(session.accessToken, session.refreshToken)
                        } else null
                    }
                    refreshTokens {
                        val authRepository = get<AuthRepository>()
                        val result = authRepository.refreshToken()
                        result.getOrNull()?.let {
                            BearerTokens(it.accessToken, it.refreshToken)
                        }
                    }
                }
            }
            defaultRequest {
                url("http://83.147.255.205:8180/api/v1/")
            }
        }
    }
}
