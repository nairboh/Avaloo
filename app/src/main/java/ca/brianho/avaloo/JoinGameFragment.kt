package ca.brianho.avaloo

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.network.*
import com.google.zxing.integration.android.IntentIntegrator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*

class JoinGameFragment : Fragment(), AnkoLogger {
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
        val server = AvalooAPI.create()
        val joinGameRequest = JoinGameRequest(playerId, name, gameId)

        server.joinGame(joinGameRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe (
                        { handleResponseSuccess(it) },
                        { handleResponseError(it) }
                )
    }

    private fun handleResponseSuccess(joinResponse: JoinGameResponse) {
        val gameState = joinResponse.gameState

        if (gameState.isBlank()) {
            error("Game State is Empty!")
        } else {
            debug("Received gameId: " + gameState)
            runOnUiThread { toast(gameState) }
        }
    }

    private fun handleResponseError(error: Throwable) {
        toast("Unable to connect to the server")
        error("Network Error: ", error)
    }
}
