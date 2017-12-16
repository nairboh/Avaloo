package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_create_game.*
import org.jetbrains.anko.*
import android.support.v7.widget.LinearLayoutManager
import ca.brianho.avaloo.activities.GameActivity
import ca.brianho.avaloo.activities.UserPromptActivity
import ca.brianho.avaloo.adapters.PlayerListAdapter
import ca.brianho.avaloo.models.*
import ca.brianho.avaloo.utils.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.json.JSONObject

class CreateGameFragment : Fragment(), AnkoLogger {
    private var qrCodeGenerated = false
    private lateinit var rxBusDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_create_game, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
        rxBusDisposable = RxEventBus.subscribe(Consumer { handleResponseMessage(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::rxBusDisposable.isInitialized) {
            rxBusDisposable.dispose()
        }
    }

    private fun setupViewsAndListeners() {
        playerRecyclerView.adapter = PlayerListAdapter(player)
        playerRecyclerView.layoutManager = LinearLayoutManager(activity)
        startGameButton.setOnClickListener { sendStartGameRequest() }
    }

    private fun sendStartGameRequest() {
        if (qrCodeGenerated) {
            val playerOrder = (playerRecyclerView.adapter as PlayerListAdapter).getItems()
            if (playerOrder.size < Game.minNumPlayers) {
                toast("Not enough players to start the game!")
            } else {
                MoshiInstance.sendRequestAsJson(
                    StartGameRequest(gameId = Game.gameId, playerOrder = playerOrder)
                )
            }
        } else {
            toast("Waiting for QR Code to generate")
        }
    }

    private fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.CREATE.name -> handleCreateGameResponse(message)
            MessageType.JOIN.name -> handlePlayerJoinResponse(message)
            MessageType.START.name -> handleStartGameResponse()
            else -> error("Unsupported message type")
        }
    }

    private fun handleCreateGameResponse(message: String) {
        try {
            val createGameResponse = MoshiInstance.fromJson<CreateGameResponse>(message)
            if (createGameResponse.gameId.isBlank()) {
                error("Game Id is blank!")
            } else {
                Game.gameId = createGameResponse.gameId
                generateAndDisplayQRCode()
            }
        } catch (e: NullPointerException) {
            error("Unable to display gameId: ", e)
        }
    }

    private fun generateAndDisplayQRCode() {
        debug("Generating QRCode based on gameId: " + Game.gameId)

        try {
            val bitMatrix = QRCodeWriter().encode(Game.gameId, BarcodeFormat.QR_CODE,400,400)
            val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
            runOnUiThread {
                qrCodeProgressBar.visibility = View.GONE
                qrCodeGeneratingTextView.visibility = View.GONE
                expanded_qrcode.setImageBitmap(bitmap)
                qrCodeGenerated = true
            }
        } catch (e: WriterException) {
            error("Unable to generate QRCode: ", e)
        }
    }

    private fun handlePlayerJoinResponse(message: String) {
        try {
            val playerJoinResponse = MoshiInstance.fromJson<PlayerJoinResponse>(message)
            runOnUiThread {
                (playerRecyclerView.adapter as PlayerListAdapter).add(playerJoinResponse.player)
            }
        } catch (e: NullPointerException) {
            error("Unable to display player join: ", e)
        }
    }

    private fun handleStartGameResponse() {
        startActivity(intentFor<GameActivity>().noAnimation())
        startActivity<UserPromptActivity>(
            getString(R.string.key_type) to getString(R.string.key_select_special)
        )
    }
}
