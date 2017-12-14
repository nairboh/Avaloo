package ca.brianho.avaloo.network

import android.util.Log
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WSListener : WebSocketListener() {
    private lateinit var inter: WSInterface

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        Log.e("dam","WebSocket message received: " + text)
        inter.handleResponseMessage(text)
    }

    fun setInterface(inter: WSInterface) {
        this.inter = inter
    }
}

interface WSInterface {
    fun handleResponseMessage(message: String?)
}