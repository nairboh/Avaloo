package ca.brianho.avaloo.activities

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import ca.brianho.avaloo.R
import ca.brianho.avaloo.fragments.lobby.LobbyFragment
import ca.brianho.avaloo.fragments.lobby.SetupPlayerFragment
import ca.brianho.avaloo.network.Player
import ca.brianho.avaloo.utils.*
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences

class LobbyActivity : Activity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val savedPlayerJson = defaultSharedPreferences.getString(getString(R.string.key_player), null)
        if (savedPlayerJson != null) {
            val savedPlayer = moshi.adapter<Player>(Player::class.java).fromJson(savedPlayerJson)

            if (savedPlayer != null) {
                player = savedPlayer
            }
        }

        replaceFragment(R.id.fragment_container, when (savedPlayerJson) {
            null -> SetupPlayerFragment()
            else -> LobbyFragment()
        })
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Abandoning Game")
                    .setMessage("Are you sure you leave this game?")
                    .setPositiveButton("Yes", { _, _ -> fragmentManager.popBackStack() })
                    .setNegativeButton("No", null)
                    .show()
        } else {
            super.onBackPressed()
        }
    }
}
