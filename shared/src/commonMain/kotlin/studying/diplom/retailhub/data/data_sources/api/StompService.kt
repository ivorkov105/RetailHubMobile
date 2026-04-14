package studying.diplom.retailhub.data.data_sources.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.enteties.request.RequestEntity
import studying.diplom.retailhub.data.enteties.request.StompRequestUpdateEntity
import studying.diplom.retailhub.data.enteties.request.toRequestEntity

class StompService(
    private val client: HttpClient,
    private val localSource: LocalSource
) {
    private var session: WebSocketSession? = null
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private val json = Json { ignoreUnknownKeys = true }

    private val _requestUpdates = MutableSharedFlow<RequestEntity>()
    val requestUpdates: SharedFlow<RequestEntity> = _requestUpdates.asSharedFlow()

    fun connect() {
        scope.launch {
            val token = localSource.getSession()?.accessToken ?: return@launch
            try {
                session = client.webSocketSession {
                    url("ws://83.147.255.205:8180/ws")
                }

                val connectFrame = """
                    CONNECT
                    accept-version:1.2
                    host:83.147.255.205
                    Authorization:Bearer $token
                    
                    \u0000
                """.trimIndent().replace("\\u0000", "\u0000")
                
                session?.send(Frame.Text(connectFrame))

                session?.incoming?.consumeAsFlow()
                    ?.filterIsInstance<Frame.Text>()
                    ?.onEach { frame ->
                        handleFrame(frame.readText())
                    }
                    ?.launchIn(this)

            } catch (e: Exception) {
                println("Stomp Connection Error: ${e.message}")
                delay(5000)
                connect()
            }
        }
    }

    fun subscribeToStore(storeId: String) {
        val subscribeFrame = """
            SUBSCRIBE
            id:sub-store
            destination:/topic/store/$storeId/requests
            
            \u0000
        """.trimIndent().replace("\\u0000", "\u0000")
        
        scope.launch {
            session?.send(Frame.Text(subscribeFrame))
        }
    }

    fun subscribeToDepartment(departmentId: String) {
        val subscribeFrame = """
            SUBSCRIBE
            id:sub-dept
            destination:/topic/department/$departmentId/requests
            
            \u0000
        """.trimIndent().replace("\\u0000", "\u0000")
        
        scope.launch {
            session?.send(Frame.Text(subscribeFrame))
        }
    }

    private fun handleFrame(frameText: String) {
        if (frameText.startsWith("MESSAGE")) {
            val body = frameText.substringAfter("\n\n").trimEnd('\u0000')
            try {
                val update = json.decodeFromString<StompRequestUpdateEntity>(body)
                scope.launch {
                    _requestUpdates.emit(update.toRequestEntity())
                }
            } catch (e: Exception) {
                println("Error decoding STOMP message: ${e.message}")
            }
        }
    }

    fun disconnect() {
        scope.launch {
            session?.close()
            session = null
        }
    }
}
