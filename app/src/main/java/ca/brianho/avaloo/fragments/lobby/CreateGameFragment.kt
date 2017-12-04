package ca.brianho.avaloo.fragments.lobby

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
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import ca.brianho.avaloo.adapters.PlayerListAdapter
import ca.brianho.avaloo.network.Player
import com.squareup.moshi.Json
import kotlinx.android.synthetic.main.viewholder_player.*
import org.json.JSONArray
import org.json.JSONObject

class CreateGameFragment : Fragment(), AnkoLogger {
    lateinit var gameId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendCreateGameRequest()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_create_game, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerRecyclerView.adapter = PlayerListAdapter()
        playerRecyclerView.layoutManager = LinearLayoutManager(activity)

        startGameButton.setOnClickListener{ sendStartGameRequest() }
    }

    private fun sendCreateGameRequest() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter<StartGameRequest>(StartGameRequest::class.java)
        val startGameRequestJson = adapter.toJson(StartGameRequest(RequestTypes.CREATE.name, playerId, name))

        val client = OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()
        val request = Request.Builder().url(getString(R.string.websocket_uri)).build()
        websocket = client.newWebSocket(request, WSListener())
        websocket.send(startGameRequestJson)
    }

    private fun sendStartGameRequest() {
        val playerList = mutableListOf(playerId)
        (playerRecyclerView.adapter as PlayerListAdapter).getItems().forEach {
            playerList.add(it.playerId)
        }
        val jsonArray = JSONArray(playerList)
        val json = JSONObject()
        json.put("type", "PRE_GAME_INFO")
        json.put("playerOrder", jsonArray)
        json.put("gameId", gameId)
        Log.e("JSON STRING", json.toString())
        websocket.send(json.toString())
    }

    private fun handleResponseMessage(message: String?) {
        if (message.isNullOrBlank()) {
            error("WebSocket message is blank!")
        } else {
            val json = JSONObject(message)
            when (json["type"]) {
                "CREATE" -> handleCreateGame(message)
                "JOIN" -> handleJoinGame(json)
                else -> handleResponseFailure()
            }
        }
    }

    private fun handleCreateGame(message: String?) {
        val adapter = moshi.adapter<StartGameResponse>(StartGameResponse::class.java)
        val startGameResponse = adapter.fromJson(message)

        if (startGameResponse == null) {
            handleResponseFailure()
        } else {
            handleResponseSuccess(startGameResponse)
        }
    }

    private fun handleJoinGame(json: JSONObject) {
        runOnUiThread {
            (playerRecyclerView.adapter as PlayerListAdapter).add(json)
        }
    }

    private fun handleResponseSuccess(gameResponse: StartGameResponse) {
        gameId = gameResponse.gameId

        if (gameId.isBlank()) {
            error("Game Id is blank!")
        } else {
            generateAndDisplayQRCode()
        }
    }

    private fun handleResponseFailure() {
        error("Invalid WebSocket message!")
    }

    private fun generateAndDisplayQRCode() {
        debug("Generating QRCode based on gameId: " + gameId)

        try {
            val bitMatrix = QRCodeWriter().encode(gameId, BarcodeFormat.QR_CODE,400,400)
            val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
            runOnUiThread { expanded_qrcode.setImageBitmap(bitmap) }
        } catch (e: WriterException) {
            error("QRCode generation error: ", e)
        }
    }

    private inner class WSListener : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Log.e("dam","WebSocket message received: " + text)
            handleResponseMessage(text)
        }
    }
}
