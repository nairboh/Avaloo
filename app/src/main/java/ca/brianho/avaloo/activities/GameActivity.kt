package ca.brianho.avaloo.activities

import android.app.Activity
import android.os.Bundle
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.game.setup.SpecialRolesFragment
import ca.brianho.avaloo.utils.replaceFragment

class GameActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        replaceFragment(R.id.fragment_container, SpecialRolesFragment())
    }
}
