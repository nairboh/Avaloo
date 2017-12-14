package ca.brianho.avaloo.fragments.game

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.activities.UserPromptActivity
import ca.brianho.avaloo.adapters.PartyListAdapter
import ca.brianho.avaloo.network.PartyChoiceRequest
import ca.brianho.avaloo.network.Player
import ca.brianho.avaloo.network.WSInterface
import ca.brianho.avaloo.utils.*
import kotlinx.android.synthetic.main.fragment_board.*
import org.json.JSONArray
import org.json.JSONObject

class BoardFragment : Fragment(), WSInterface {
    private var message = ""
    private var numMembers = 0

    override fun handleResponseMessage(message: String?) {
        this.message = message!!


        val json = JSONObject(message)

        when (json["type"].toString()) {
            "PARTY_VOTE" -> {
                val intent = Intent(activity, UserPromptActivity::class.java)
                intent.putExtra("TYPE", "VOTE")
                startActivity(intent)
            }
            "QUEST_INFO" -> {
                numMembers = Integer.parseInt(json["partySize"].toString())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = PartyListAdapter(playerList, numMembers)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.visibility = View.VISIBLE

        button.setOnClickListener { sendPartyChoiceRequest() }
        questInfoTextView.text = message
    }

    private fun sendPartyChoiceRequest() {
        val members = (recyclerView.adapter as PartyListAdapter).getSelectedPlayers()
        val partyChoiceRequest = PartyChoiceRequest(gameId = gameId, members = members)
        websocket.sendJson(partyChoiceRequest)
    }
}
