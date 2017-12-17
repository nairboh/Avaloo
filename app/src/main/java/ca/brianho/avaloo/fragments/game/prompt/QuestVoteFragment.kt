package ca.brianho.avaloo.fragments.game.prompt

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.*
import ca.brianho.avaloo.utils.MoshiInstance
import ca.brianho.avaloo.utils.role
import kotlinx.android.synthetic.main.fragment_vote.*

class QuestVoteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_vote, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        approveButton.setOnClickListener { sendPartyVoteRequest(it) }
        declineButton.setOnClickListener { sendPartyVoteRequest(it) }

//        if (role.team == "Good") {
//            declineButton.text = getString(R.string.approve)
//        }
    }

    private fun sendPartyVoteRequest(view: View) {
        val vote: String = when (view.id) {
            R.id.approveButton -> "approve"
            R.id.declineButton -> "decline"
            else -> error("Invalid view")
        }

        MoshiInstance.sendRequestAsJson(QuestVoteRequest(gameId = Game.gameId, vote = vote))
        activity.finish()
    }
}
