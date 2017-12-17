package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.activities.GameActivity
import ca.brianho.avaloo.models.Game
import ca.brianho.avaloo.models.JoinGameRequest
import ca.brianho.avaloo.models.JoinGameResponse
import ca.brianho.avaloo.models.MessageType
import ca.brianho.avaloo.utils.*
import com.google.zxing.integration.android.IntentIntegrator
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_join_game.*
import org.jetbrains.anko.*
import org.json.JSONObject

class JoinGameFragment : Fragment(), AnkoLogger {
    private lateinit var rxBusDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxBusDisposable = RxEventBus.subscribe(Consumer { handleResponseMessage(it) })
        setupAndStartScanner()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_join_game, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::rxBusDisposable.isInitialized) {
            rxBusDisposable.dispose()
        }
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
                    //Temp
                    Game.gameId = content
                    MoshiInstance.sendRequestAsJson(
                        JoinGameRequest(MessageType.JOIN.name, player, content)
                    )
                } else {
                    toast("Scanned content is invalid!")
                }
            }
        } else {
            fragmentManager.popBackStack()
        }
    }

    private fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.JOIN.name -> handleJoinGameResponse(message)
            MessageType.CLIENT_SETUP.name -> handleClientSetupResponse()
            else -> error("Unsupported message type")
        }
    }

    private fun handleJoinGameResponse(message: String) {
        try {
            handleResponseSuccess(MoshiInstance.fromJson(message))
        } catch (e: NullPointerException) {
            error("Unable to join game: ", e)
        }
    }

    private fun handleClientSetupResponse() {
        startActivity<GameActivity>()
    }

    private fun handleResponseSuccess(joinResponse: JoinGameResponse) {
        val gameState = joinResponse.gameState

        if (gameState.isBlank()) {
            error("Game State is blank!")
        } else {
            debug("Received gameId: " + gameState)
            runOnUiThread {
                textView.visibility = View.VISIBLE
                toast(gameState)
            }
        }
    }
}
