package ca.brianho.avaloo

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.network.AvalooAPI
import ca.brianho.avaloo.network.StartGameRequest
import ca.brianho.avaloo.network.StartGameResponse
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_create_game.*
import org.jetbrains.anko.*

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
        val server = AvalooAPI.create()
        val startGameRequest = StartGameRequest(playerId, name)

        server.startGame(startGameRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe (
                        { handleResponseSuccess(it) },
                        { handleResponseError(it) }
                )
    }

    private fun handleResponseSuccess(gameResponse: StartGameResponse) {
        val gameId = gameResponse.gameId

        if (gameId.isBlank()) {
            error("Game Id is Empty!")
        } else {
            debug("Received gameId: " + gameId)

            try {
                val bitMatrix = QRCodeWriter().encode(gameId, BarcodeFormat.QR_CODE,400,400)
                val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
                runOnUiThread { qrcode.setImageBitmap(bitmap) }
            } catch (e: WriterException) {
                error("QRCode generation error: ", e)
            }
        }
    }

    private fun handleResponseError(error: Throwable) {
        toast("Unable to connect to the server")
        error("Network Error: ", error)
    }
}
