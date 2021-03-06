package ca.brianho.avaloo2.activities

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import ca.brianho.avaloo2.R
import ca.brianho.avaloo2.fragments.lobby.LobbyFragment
import ca.brianho.avaloo2.fragments.lobby.SetupPlayerFragment
import ca.brianho.avaloo2.models.Player
import ca.brianho.avaloo2.network.WSConnection
import ca.brianho.avaloo2.utils.*
import org.jetbrains.anko.*

class LobbyActivity : Activity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val savedPlayerJson = defaultSharedPreferences.getString(getString(R.string.key_player), null)
        if (savedPlayerJson != null) {
            val savedPlayer = MoshiInstance.fromJson<Player>(savedPlayerJson)
            player = savedPlayer
        }

        replaceFragment(R.id.fragment_container, when (savedPlayerJson) {
            null -> SetupPlayerFragment()
            else -> LobbyFragment()
        })
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            alert(getString(R.string.message_leave_game_confirm),
                    getString(R.string.message_leave_game_confirm_title)) {
                yesButton {
                    WSConnection.disconnect()
                    RxEventBus.clear()
                    fragmentManager.popBackStack()
                }
                noButton {}
            }.show()
        } else {
            super.onBackPressed()
        }
    }
}
