package ca.brianho.avaloo.activities

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.game.prompt.VoteFragment
import ca.brianho.avaloo.fragments.game.setup.SpecialRolesFragment
import ca.brianho.avaloo.utils.replaceFragment

class UserPromptActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)


        val fragment: Fragment = when (intent.extras["TYPE"]) {
            "SELECT_SPECIAL" -> SpecialRolesFragment()
            "VOTE" -> VoteFragment()
            else -> throw error("ERROR WRONG SELECTION")
        }

        replaceFragment(R.id.fragment_container, fragment)
    }
}
