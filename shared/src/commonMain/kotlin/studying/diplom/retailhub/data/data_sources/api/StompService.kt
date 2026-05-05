package studying.diplom.retailhub.data.data_sources.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import studying.diplom.retailhub.data.data_sources.LocalSource
import studying.diplom.retailhub.data.entities.request.RequestEntity
import studying.diplom.retailhub.data.entities.request.StompRequestUpdateEntity
import studying.diplom.retailhub.data.entities.request.toRequestEntity

private const val URL = "wss://83.147.255.205/ws-native"
private const val LOG_TAG = "[STOMP]"

class StompService(
	private val client: HttpClient,
	private val localSource: LocalSource,
	private val json: Json,
	private val logger: Logger
) : WSService {

	private var session: WebSocketSession? = null
	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
	private var heartbeatJob: Job? = null
	private var connectionJob: Job? = null

	private val isConnected = MutableStateFlow(false)

	// Храним активные подписки: ID -> Destination
	private val activeSubscriptions = mutableMapOf<String, String>()

	private val _requestUpdates = MutableSharedFlow<RequestEntity>()
	override val requestUpdates: SharedFlow<RequestEntity> = _requestUpdates.asSharedFlow()

	override fun connect() {
		if (isConnected.value || connectionJob?.isActive == true) return

		connectionJob = scope.launch {
			while (isActive && !isConnected.value) {
				val userSession = localSource.getSession()
				val token = userSession?.accessToken

				if (token == null) {
					logger.log("$LOG_TAG Auth token missing. Retry in 5s...")
					delay(5000)
					continue
				}

				try {
					logger.log("$LOG_TAG Connecting to $URL...")
					session = client.webSocketSession {
						url(URL)
						header("Sec-WebSocket-Protocol", "v12.stomp, v11.stomp, v10.stomp")
					}

					val incomingJob = launch { observeIncoming() }

					sendConnectFrame(token)

					try {
						withTimeout(5000) {
							isConnected.first { it }
						}
						logger.log("$LOG_TAG STOMP Connected & Ready.")
						incomingJob.join()
					} catch (e: Exception) {
						logger.log("$LOG_TAG STOMP Handshake timeout or error: ${e.message}")
						cleanup()
						delay(5000)
					}
				} catch (e: Exception) {
					logger.log("$LOG_TAG Connection failed: ${e.message}. Retrying in 5s...")
					cleanup()
					delay(5000)
				}
			}
		}
	}

	private suspend fun observeIncoming() {
		try {
			session?.incoming?.receiveAsFlow()
				?.filterIsInstance<Frame.Text>()
				?.collect { frame ->
					val text = frame.readText()
					if (text == "\n" || text == "\r\n") return@collect
					logFrame("RECV", text)
					handleFrame(text)
				}
		} catch (e: Exception) {
			logger.log("$LOG_TAG Session closed: ${e.message}")
		} finally {
			cleanup()
		}
	}

	private suspend fun sendConnectFrame(token: String) {
		val headers = mapOf(
			"accept-version" to "1.2",
			"host" to "83.147.255.205",
			"Authorization" to "Bearer $token",
			"heart-beat" to "10000,10000"
		)
		sendStompFrame("CONNECT", headers)
	}

	private suspend fun sendStompFrame(command: String, headers: Map<String, String>, body: String = "") {
		val frameText = buildString {
			append(command).append("\n")
			headers.forEach { (key, value) -> append(key).append(":").append(value).append("\n") }
			append("\n")
			append(body)
			append("\u0000")
		}

		try {
			session?.send(Frame.Text(frameText))
			logFrame("SEND", frameText)
		} catch (e: Exception) {
			logger.log("$LOG_TAG Send error: ${e.message}")
			cleanup()
		}
	}

	private fun startHeartbeatLoop() {
		heartbeatJob?.cancel()
		heartbeatJob = scope.launch {
			while (isActive && isConnected.value) {
				delay(10000)
				try {
					session?.send(Frame.Text("\n"))
				} catch (e: Exception) {
					logger.log("$LOG_TAG Heartbeat failed: ${e.message}")
					cleanup()
				}
			}
		}
	}

	override fun subscribeToStore(storeId: String) {
		subscribe("/topic/store/$storeId/requests", "sub-store-$storeId")
	}

	override fun subscribeToDepartment(departmentId: String) {
		subscribe("/topic/department/$departmentId/requests", "sub-dept-$departmentId")
	}

	override fun unsubscribeFromStore(storeId: String) {
		unsubscribe("sub-store-$storeId")
	}

	override fun unsubscribeFromDepartment(departmentId: String) {
		unsubscribe("sub-dept-$departmentId")
	}

	private fun subscribe(destination: String, id: String) {
		activeSubscriptions[id] = destination
		scope.launch {
			if (!isConnected.value) connect()
			isConnected.first { it }
			sendStompFrame(
				"SUBSCRIBE", mapOf(
					"id" to id,
					"destination" to destination,
					"ack" to "auto"
				)
			)
		}
	}

	private fun unsubscribe(id: String) {
		activeSubscriptions.remove(id)
		scope.launch {
			if (isConnected.value) {
				sendStompFrame("UNSUBSCRIBE", mapOf("id" to id))
			}
		}
	}

	private fun handleFrame(frameText: String) {
		when {
			frameText.startsWith("CONNECTED") -> {
				isConnected.value = true
				startHeartbeatLoop()
				resubscribeAll()
			}

			frameText.startsWith("MESSAGE")   -> parseMessage(frameText)
			frameText.startsWith("ERROR")     -> logger.log("$LOG_TAG SERVER ERROR: $frameText")
		}
	}

	private fun resubscribeAll() {
		val subs = activeSubscriptions.toMap()
		subs.forEach { (id, dest) ->
			scope.launch {
				if (isConnected.value) {
					sendStompFrame(
						"SUBSCRIBE", mapOf(
							"id" to id,
							"destination" to dest,
							"ack" to "auto"
						)
					)
				}
			}
		}
	}

	private fun parseMessage(frameText: String) {
		val body = frameText.substringAfter("\n\n").trimEnd('\u0000')
		if (body.isBlank()) return

		try {
			val update = json.decodeFromString<StompRequestUpdateEntity>(body)
			scope.launch { _requestUpdates.emit(update.toRequestEntity()) }
		} catch (e: Exception) {
			logger.log("$LOG_TAG Parse error: ${e.message}")
		}
	}

	private suspend fun cleanup() {
		val oldSession = session
		session = null
		isConnected.value = false
		heartbeatJob?.cancel()
		try {
			oldSession?.close()
		} catch (e: Exception) {
			logger.log("$LOG_TAG Cleanup error: ${e.message}")
		}
	}

	private fun logFrame(prefix: String, text: String) {
		val cleanText = text.replace("\u0000", "[NULL]").trim()
		logger.log("$LOG_TAG $prefix:\n$cleanText\n---------------------------")
	}

	override fun disconnect() {
		scope.launch {
			connectionJob?.cancel()
			cleanup()
			logger.log("$LOG_TAG Disconnected manually.")
		}
	}
}
