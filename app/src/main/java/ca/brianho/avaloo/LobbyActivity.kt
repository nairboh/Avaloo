package ca.brianho.avaloo

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.info

class LobbyActivity : Activity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        val name = defaultSharedPreferences.getString(getString(R.string.key_name), null)

        if (name == null) {
            replaceFragment(R.id.fragment_container, NameFragment())
        } else {
            info("Saved name is " + name)
        }
    }
}
