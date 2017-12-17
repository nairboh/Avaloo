package ca.brianho.avaloo.activities

import android.app.Activity
import android.os.Bundle
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.game.prompt.PartyVoteFragment
import ca.brianho.avaloo.fragments.game.prompt.QuestVoteFragment
import ca.brianho.avaloo.fragments.game.setup.SpecialRolesFragment
import ca.brianho.avaloo.network.WSConnection
import ca.brianho.avaloo.utils.replaceFragment
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class UserPromptActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        replaceFragment(R.id.fragment_container, when (intent.extras[getString(R.string.key_type)]) {
            "SELECT_SPECIAL" -> SpecialRolesFragment()
            "VOTE_PARTY" -> PartyVoteFragment()
            "VOTE_QUEST" -> QuestVoteFragment()
            else -> error("Invalid fragment intent")
        })
    }

    override fun onBackPressed() {
        alert(getString(R.string.message_leave_game_confirm),
                getString(R.string.message_leave_game_confirm_title)) {
            yesButton {
                WSConnection.disconnect()
                super.onBackPressed()
            }
            noButton {}
        }.show()
    }
}
