package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.CreateGameRequest
import ca.brianho.avaloo.utils.MoshiInstance
import ca.brianho.avaloo.utils.RxEventBus
import ca.brianho.avaloo.utils.player
import ca.brianho.avaloo.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_create_or_join.*

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
        activity.replaceFragment(R.id.fragment_container, when (view) {
            createGameButton -> {
                RxEventBus.clear()
                MoshiInstance.sendRequestAsJson(CreateGameRequest(player = player))
                CreateGameFragment()
            }
            joinGameButton -> JoinGameFragment()
            settingsButton -> SetupPlayerFragment()
            else -> error("Unsupported View")
        }, true)
    }
}
