package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.moshi.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.fragment_create_game.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi
import android.support.v7.widget.LinearLayoutManager
import ca.brianho.avaloo.activities.GameActivity
import ca.brianho.avaloo.activities.UserPromptActivity
import ca.brianho.avaloo.adapters.PlayerListAdapter
import ca.brianho.avaloo.fragments.game.RoleFragment
import ca.brianho.avaloo.network.*
import ca.brianho.avaloo.utils.*
import org.json.JSONArray
import org.json.JSONObject


class CreateGameFragment : Fragment(), AnkoLogger, WSInterface {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_create_game, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerRecyclerView.adapter = PlayerListAdapter(player)
        playerRecyclerView.layoutManager = LinearLayoutManager(activity)
        sendCreateGameRequest()
        startGameButton.setOnClickListener { sendStartGameRequest() }
    }

    private fun sendCreateGameRequest() {
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val client = OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()
        val request = Request.Builder().url(getString(R.string.websocket_uri)).build()

        wsListener = WSListener()
        wsListener.setInterface(this)

        val gameRequest = CreateGameRequest(player = player)
        websocket = client.newWebSocket(request, wsListener)
        websocket.sendJson(gameRequest)
    }

    private fun sendStartGameRequest() {
        if (qrCodeProgressBar.visibility == View.VISIBLE) {
            toast("Waiting for QR Code to generate")
        } else {
            val playerOrder = (playerRecyclerView.adapter as PlayerListAdapter).getItems()
            if (playerOrder.size < createGameResponse.minNumPlayers) {
                toast("Not enough players to start the game!")
            } else {
                val startGameRequest = StartGameRequest(
                        gameId = createGameResponse.gameId, playerOrder = playerOrder)
                websocket.sendJson(startGameRequest)
            }
        }
    }

    override fun handleResponseMessage(message: String?) {
        if (message.isNullOrBlank()) {
            error("WebSocket message is blank!")
        } else {
            val json = JSONObject(message)
            when (json["type"]) {
                MessageType.CREATE.name -> handleCreateGameResponse(message)
                MessageType.JOIN.name -> handlePlayerJoinResponse(message)
                "PRE_GAME_INFO" -> {
                    roles = mutableListOf()
                    val jsonArray = JSONArray(json["roles"].toString())

                    numGood = Integer.parseInt(json["numGood"].toString())
                    numEvil = Integer.parseInt(json["numEvil"].toString())

                    val adapter = moshi.adapter<Role>(Role::class.java)
                    for (i in 0 until jsonArray.length()) {
                        roles.add(adapter.fromJson(jsonArray.getJSONObject(i).toString())!!)
                    }

                    val gameActivityIntent = Intent(activity, GameActivity::class.java)
                    gameActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(gameActivityIntent)

                    roleFragment = RoleFragment()
                    wsListener.setInterface(roleFragment)

                    val intent = Intent(activity, UserPromptActivity::class.java)
                    intent.putExtra("TYPE", "SELECT_SPECIAL")
                    startActivity(intent)
                }
                else -> error("Unsupported message type")
            }
        }
    }

    private fun handleCreateGameResponse(message: String?) {
        try {
            createGameResponse = moshi.fromJson(message)
            if (createGameResponse.gameId.isBlank()) {
                error("Game Id is blank!")
            } else {
                generateAndDisplayQRCode()
            }
        } catch (e: NullPointerException) {
            handleResponseFailure()
        }
    }

    private fun handlePlayerJoinResponse(message: String?) {
        try {
            val playerJoinResponse = moshi.fromJson<PlayerJoinResponse>(message)
            runOnUiThread {
                (playerRecyclerView.adapter as PlayerListAdapter).add(playerJoinResponse.player)
            }
        } catch (e: NullPointerException) {
            handleResponseFailure()
        }
    }

    private fun handleResponseFailure() {
        error("Invalid WebSocket message!")
    }

    private fun generateAndDisplayQRCode() {
        val gameId = createGameResponse.gameId
        debug("Generating QRCode based on gameId: " + gameId)

        try {
            val bitMatrix = QRCodeWriter().encode(gameId, BarcodeFormat.QR_CODE,400,400)
            val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
            runOnUiThread {
                qrCodeProgressBar.visibility = View.GONE
                qrCodeGeneratingTextView.visibility = View.GONE
                expanded_qrcode.setImageBitmap(bitmap)
            }
        } catch (e: WriterException) {
            error("QRCode generation error: ", e)
        }
    }
}
