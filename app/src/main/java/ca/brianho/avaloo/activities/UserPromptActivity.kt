package ca.brianho.avaloo.activities

import android.app.Activity
import android.os.Bundle
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.game.prompt.VoteFragment
import ca.brianho.avaloo.fragments.game.setup.SpecialRolesFragment
import ca.brianho.avaloo.utils.replaceFragment
import org.jetbrains.anko.toast

class UserPromptActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        replaceFragment(R.id.fragment_container, when (intent.extras[getString(R.string.key_type)]) {
            "SELECT_SPECIAL" -> SpecialRolesFragment()
            "VOTE" -> VoteFragment()
            else -> error("Invalid fragment intent")
        })
    }

    override fun onBackPressed() {
        toast("You may not go back to the previous screen")
    }
}
