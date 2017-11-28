package ca.brianho.avaloo.fragments

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.utils.name
import ca.brianho.avaloo.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_create_or_join.*

class CreateOrJoinFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_or_join, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcomeText.text = getString(R.string.welcome_msg, name)
        createGameButton.setOnClickListener { handleClick(it) }
        joinGameButton.setOnClickListener { handleClick(it) }
    }

    private fun handleClick(view: View) {
        val fragment: Fragment = when (view) {
            createGameButton -> CreateGameFragment()
            joinGameButton -> JoinGameFragment()
            else -> throw Error("View is not mapped to a destination properly!")
        }

        activity.replaceFragment(R.id.fragment_container, fragment)
    }
}
