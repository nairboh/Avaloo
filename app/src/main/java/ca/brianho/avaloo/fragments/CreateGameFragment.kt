package ca.brianho.avaloo.fragments

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.utils.name
import ca.brianho.avaloo.network.RequestTypes
import ca.brianho.avaloo.network.StartGameRequest
import ca.brianho.avaloo.network.StartGameResponse
import ca.brianho.avaloo.utils.moshi
import ca.brianho.avaloo.utils.playerId
import ca.brianho.avaloo.utils.websocket
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.moshi.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.fragment_create_game.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi

class CreateGameFragment : Fragment(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendStartGameRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_create_game, container, false)
    }

    private fun sendStartGameRequest() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter<StartGameRequest>(StartGameRequest::class.java)
        val startGameRequestJson = adapter.toJson(StartGameRequest(RequestTypes.CREATE.name, playerId, name))

        val client = OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()
        val request = Request.Builder().url(getString(R.string.websocket_uri)).build()
        websocket = client.newWebSocket(request, WSListener())
        websocket.send(startGameRequestJson)
    }

    private fun handleResponseMessage(message: String?) {
        if (message.isNullOrBlank()) {
            error("WebSocket message is blank!")
        } else {
            val adapter = moshi.adapter<StartGameResponse>(StartGameResponse::class.java)
            val startGameResponse = adapter.fromJson(message)

            if (startGameResponse == null) {
                handleResponseFailure()
            } else {
                handleResponseSuccess(startGameResponse)
            }
        }
    }

    private fun handleResponseSuccess(gameResponse: StartGameResponse) {
        val gameId = gameResponse.gameId

        if (gameId.isBlank()) {
            error("Game Id is blank!")
        } else {
            generateAndDisplayQRCode(gameId)
        }
    }

    private fun handleResponseFailure() {
        error("Invalid WebSocket message!")
    }

    private fun generateAndDisplayQRCode(gameId: String) {
        debug("Generating QRCode based on gameId: " + gameId)

        try {
            val bitMatrix = QRCodeWriter().encode(gameId, BarcodeFormat.QR_CODE,400,400)
            val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
            runOnUiThread { qrcode.setImageBitmap(bitmap) }
        } catch (e: WriterException) {
            error("QRCode generation error: ", e)
        }
    }

    private inner class WSListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket?, text: String?) {
            debug("WebSocket message received: " + text)
            handleResponseMessage(text)
        }
    }
}
