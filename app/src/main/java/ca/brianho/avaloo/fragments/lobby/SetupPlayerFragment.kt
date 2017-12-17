package ca.brianho.avaloo.fragments.lobby

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.Player
import ca.brianho.avaloo.utils.*
import kotlinx.android.synthetic.main.fragment_name.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.toast
import java.util.*

class SetupPlayerFragment : Fragment(), AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_name, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameField.setOnEditorActionListener {
            _, actionId, _ -> onEnterPressed(actionId)
        }
    }

    private fun onEnterPressed(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (nameField.text.isNullOrBlank()) {
                toast(getString(R.string.toast_valid_name))
            } else {
                player = Player(nameField.text.toString(), UUID.randomUUID().toString())
                defaultSharedPreferences.edit()
                        .putString(getString(R.string.key_player), MoshiInstance.toJson(player))
                        .apply()

                activity.replaceFragment(R.id.fragment_container, LobbyFragment())
            }
        }
        return true
    }
}
