package ca.brianho.avaloo.network

import ca.brianho.avaloo.utils.RxEventBus
import okhttp3.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import java.util.concurrent.TimeUnit

object WSConnection : AnkoLogger {
    private var websocket: WebSocket? = null

    private fun connect(url: String = "ws://avaloo-server.herokuapp.com/") {
        val client = OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()
        val request = Request.Builder().url(url).build()
        websocket = client.newWebSocket(request, WSListener)
    }

    fun disconnect() {
        if (websocket != null) {
            websocket!!.close(1000, "Disconnected")
            websocket = null
        }
    }

    fun send(message: String) {
        if (websocket == null) {
            connect()
        }

        websocket!!.send(message)
    }

    private object WSListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket?, message: String?) {
            debug("WebSocket message received: " + message)

            if (message == null || message.isBlank()) {
                error("Websocket message is null or blank")
            } else {
                RxEventBus.onNext(message)
            }
        }
    }
}