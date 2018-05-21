package ca.brianho.avaloo2.fragments.game.prompt

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo2.R
import ca.brianho.avaloo2.models.*
import ca.brianho.avaloo2.utils.MoshiInstance
import kotlinx.android.synthetic.main.fragment_choose.*
import org.jetbrains.anko.toast

class QuestVoteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        infoTextView.text = getString(R.string.quest_prompt_text)
        playerPartyVoteSpinner.visibility = View.GONE
        playerQuestVoteSpinner.visibility = View.VISIBLE

        submitButton.setOnClickListener { sendQuestVoteRequest() }
    }

    private fun sendQuestVoteRequest() {
        val vote: String = playerQuestVoteSpinner.selectedItem.toString()

        if (vote == "Choose") {
            toast("Please make a decision first")
            return
        }

        MoshiInstance.sendRequestAsJson(QuestVoteRequest(gameId = Game.gameId, vote = vote))
        activity.finish()
    }
}
