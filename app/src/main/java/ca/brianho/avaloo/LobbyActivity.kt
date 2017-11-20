package ca.brianho.avaloo

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

class LobbyActivity : Activity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        playerId = defaultSharedPreferences.getString(getString(R.string.key_playerId),
                UUID.randomUUID().toString())

        val savedName = defaultSharedPreferences.getString(getString(R.string.key_name), null)
        if (savedName.isNullOrBlank()) {
            replaceFragment(R.id.fragment_container, NameFragment())
        } else {
            name = savedName
            replaceFragment(R.id.fragment_container, CreateOrJoinFragment())
        }
    }
}
