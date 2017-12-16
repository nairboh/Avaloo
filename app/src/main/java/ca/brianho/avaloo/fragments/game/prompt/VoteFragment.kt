package ca.brianho.avaloo.fragments.game.prompt

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.Game
import ca.brianho.avaloo.models.PartyVoteRequest
import ca.brianho.avaloo.utils.MoshiInstance
import kotlinx.android.synthetic.main.fragment_vote.*

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
        val vote: String = when (view.id) {
            R.id.approveButton -> "approve"
            R.id.declineButton -> "decline"
            else -> error("Invalid view")
        }

        MoshiInstance.sendRequestAsJson(PartyVoteRequest(gameId = Game.gameId, vote = vote))
    }
}
