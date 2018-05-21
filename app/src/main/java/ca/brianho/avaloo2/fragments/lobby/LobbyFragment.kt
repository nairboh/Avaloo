package ca.brianho.avaloo2.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo2.R
import ca.brianho.avaloo2.activities.SettingsActivity
import ca.brianho.avaloo2.models.CreateGameRequest
import ca.brianho.avaloo2.utils.MoshiInstance
import ca.brianho.avaloo2.utils.RxEventBus
import ca.brianho.avaloo2.utils.player
import ca.brianho.avaloo2.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_create_or_join.*
import org.jetbrains.anko.startActivity

class LobbyFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_or_join, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcomeText.text = getString(R.string.welcome_msg, player.alias)

        createGameButton.setOnClickListener { handleClick(it) }
        joinGameButton.setOnClickListener { handleClick(it) }
        settingsButton.setOnClickListener { handleClick(it) }
    }

    private fun handleClick(view: View) {
        when (view) {
            settingsButton -> startActivity<SettingsActivity>()
            else -> activity.replaceFragment(R.id.fragment_container, when (view) {
                createGameButton -> {
                    RxEventBus.clear()
                    MoshiInstance.sendRequestAsJson(CreateGameRequest(player = player))
                    CreateGameFragment()
                }
                joinGameButton -> JoinGameFragment()
                else -> error("Unsupported View")
            }, true)
        }
    }
}
