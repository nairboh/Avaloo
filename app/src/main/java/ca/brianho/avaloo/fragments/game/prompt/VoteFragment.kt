package ca.brianho.avaloo.fragments.game.prompt

import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.utils.createGameResponse
import ca.brianho.avaloo.utils.websocket
import kotlinx.android.synthetic.main.fragment_vote.*
import org.json.JSONObject

class VoteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        approveButton.setOnClickListener { handleVote(it) }
        declineButton.setOnClickListener { handleVote(it) }
    }

    private fun handleVote(view: View) {
        val json2 = JSONObject()
        json2.put("type", "PARTY_VOTE")
        when (view.id) {
            R.id.approveButton -> json2.put("vote", "approve")
            R.id.declineButton -> json2.put("vote", "decline")
        }
        json2.put("gameId", createGameResponse.gameId)
        Log.e("JSON STRING", json2.toString())

        websocket.send(json2.toString())
    }
}
