package ca.brianho.avaloo

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences

class LobbyActivity : Activity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        name = defaultSharedPreferences.getString(getString(R.string.key_name), null)

        replaceFragment(R.id.fragment_container,
                if (name == null) NameFragment() else CreateOrJoinFragment())
    }
}
