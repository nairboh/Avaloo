package ca.brianho.avaloo

import android.app.Activity
import android.os.Bundle

class LobbyActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        replaceFragment(R.id.fragment_container, NameFragment())
    }
}
