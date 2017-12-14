package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.network.*
import ca.brianho.avaloo.utils.*
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

class JoinGameFragment : Fragment(), AnkoLogger, WSInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAndStartScanner()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_join_game, container, false)
    }

    private fun setupAndStartScanner() {
        val intent = IntentIntegrator(activity)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setPrompt(getString(R.string.scan_msg))
                .createScanIntent()

        startActivityForResult(intent, IntentIntegrator.REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            val uuidPattern = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")

            if (result != null) {
                val content = result.contents
                debug("Scanned content: " + content)

                if (content.matches(uuidPattern)) {
                    sendJoinGameRequest(content)
                } else {
                    runOnUiThread { toast("Scanned content is invalid!") }
                }
            }
        } else {
            fragmentManager.popBackStack()
        }
    }

    private fun sendJoinGameRequest(gameId: String) {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter<JoinGameRequest>(JoinGameRequest::class.java)
        val joinGameRequestJson = adapter.toJson(JoinGameRequest(MessageType.JOIN.name, playerId, name, gameId))

        val client = OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()
        val request = Request.Builder().url(getString(R.string.websocket_uri)).build()

        wsListener = WSListener()
        wsListener.setInterface(this)

        websocket = client.newWebSocket(request, wsListener)
        websocket.send(joinGameRequestJson)
    }


    override fun handleResponseMessage(message: String?) {
        if (message.isNullOrBlank()) {
            error("WebSocket message is blank!")
        } else {
            val adapter = moshi.adapter<JoinGameResponse>(JoinGameResponse::class.java)
            val joinGameResponse = adapter.fromJson(message)

            if (joinGameResponse == null) {
                handleResponseFailure()
            } else {
                handleResponseSuccess(joinGameResponse)
            }
        }
    }

    private fun handleResponseSuccess(joinResponse: JoinGameResponse) {
        val gameState = joinResponse.gameState

        if (gameState.isBlank()) {
            error("Game State is blank!")
        } else {
            debug("Received gameId: " + gameState)
            runOnUiThread { toast(gameState) }
        }
    }

    private fun handleResponseFailure() {
        error("Invalid WebSocket message!")
    }
}
