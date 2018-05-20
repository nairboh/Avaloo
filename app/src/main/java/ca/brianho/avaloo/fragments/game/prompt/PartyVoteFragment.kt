package ca.brianho.avaloo.fragments.game.prompt

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.Game
import ca.brianho.avaloo.models.MessageType
import ca.brianho.avaloo.models.PartyVoteRequest
import ca.brianho.avaloo.models.PartyVoteResponse
import ca.brianho.avaloo.utils.MoshiInstance
import ca.brianho.avaloo.utils.RxEventBus
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_choose.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import org.json.JSONObject

class PartyVoteFragment : Fragment() {
    private lateinit var rxBusDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitButton.setOnClickListener { sendPartyVoteRequest() }
        rxBusDisposable = RxEventBus.subscribe(Consumer { handleResponseMessage(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::rxBusDisposable.isInitialized) {
            rxBusDisposable.dispose()
        }
    }

    private fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.PARTY_VOTE.name -> handlePartyVoteResponse(message)
        }
    }

    private fun handlePartyVoteResponse(message: String) {
        val partyVoteResponse = MoshiInstance.fromJson<PartyVoteResponse>(message)
        runOnUiThread {
            val membersString = partyVoteResponse.playerList.joinToString { player -> player.alias }
            infoTextView.text = getString(R.string.info_vote, membersString)
        }
    }

    private fun sendPartyVoteRequest() {
        val vote: String = playerPartyVoteSpinner.selectedItem.toString()

        if (vote == "Choose") {
            toast("Please make a decision first")
            return
        }

        MoshiInstance.sendRequestAsJson(PartyVoteRequest(gameId = Game.gameId, vote = vote))
        activity.finish()
    }
}
