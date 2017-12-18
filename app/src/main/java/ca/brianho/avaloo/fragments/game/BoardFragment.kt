package ca.brianho.avaloo.fragments.game

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.activities.UserPromptActivity
import ca.brianho.avaloo.adapters.PartyListAdapter
import ca.brianho.avaloo.models.*
import ca.brianho.avaloo.utils.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_board.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.json.JSONObject

class BoardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = PartyListAdapter()

        button.setOnClickListener { sendPartyChoiceRequest() }
    }

    internal fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.CLIENT_SETUP.name -> handleClientSetupResponse(message)
            MessageType.QUEST_INFO.name -> handleQuestInfoResponse(message)
            MessageType.PARTY_VOTE.name -> handlePartyVoteResponse()
            MessageType.PARTY_RESULT.name -> handlePartyResultResponse(message)
            MessageType.CHOOSE_TARGET.name -> handleChooseTargetResponse(message)
        }
    }

    private fun handleClientSetupResponse(message: String) {
        val clientSetupResponse = MoshiInstance.fromJson<ClientSetupResponse>(message)
        runOnUiThread {
            (recyclerView.adapter as PartyListAdapter).setPlayerList(clientSetupResponse.playerList)
        }
    }

    private fun handleQuestInfoResponse(message: String) {
        val questInfoResponse = MoshiInstance.fromJson<QuestInfoResponse>(message)
        runOnUiThread {
            questInfoTextView.text = getString(R.string.info_quest,
                    questInfoResponse.questNum,
                    questInfoResponse.questLeader.alias,
                    questInfoResponse.partySize,
                    questInfoResponse.questDeclines
            )
            if (player == questInfoResponse.questLeader) {
                (recyclerView.adapter as PartyListAdapter).setNumMembers(questInfoResponse.partySize)
                (recyclerView.adapter as PartyListAdapter).notifyDataSetChanged()
                recyclerView.visibility = View.VISIBLE
                button.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.GONE
                button.visibility = View.GONE
            }
        }
    }

    private fun handlePartyVoteResponse() {
        runOnUiThread {
            recyclerView.visibility = View.GONE
            button.visibility = View.GONE
        }
        startActivity<UserPromptActivity>(getString(R.string.key_type) to "VOTE_PARTY")
    }

    private fun handlePartyResultResponse(message: String) {
        val result = JSONObject(message)["result"]

        if (result == "accepted") { // need to check for player
            startActivity<UserPromptActivity>(getString(R.string.key_type) to "VOTE_QUEST")
        }
    }

    private fun handleChooseTargetResponse(message: String) {
        val chooseTargetResponse = MoshiInstance.fromJson<ChooseTargetResponse>(message)
        runOnUiThread {
            questInfoTextView.text = "Choose Merlin"
            recyclerView.adapter = PartyListAdapter()
            (recyclerView.adapter as PartyListAdapter).setNumMembers(1)
            (recyclerView.adapter as PartyListAdapter).setPlayerList(chooseTargetResponse.playerList)
            (recyclerView.adapter as PartyListAdapter).notifyDataSetChanged()
            recyclerView.visibility = View.VISIBLE
            button.visibility = View.VISIBLE

            button.setOnClickListener { sendTargetRequest() }
        }
    }

    private fun sendPartyChoiceRequest() {
        val members = (recyclerView.adapter as PartyListAdapter).getSelectedPlayers()
        MoshiInstance.sendRequestAsJson(
            PartyChoiceRequest(gameId = Game.gameId, members = members)
        )
    }

    private fun sendTargetRequest() {
        val target = (recyclerView.adapter as PartyListAdapter).getSelectedPlayers().iterator().next()
        MoshiInstance.sendRequestAsJson(
            ChooseTargetRequest(gameId = Game.gameId, player = target)
        )
    }
}
