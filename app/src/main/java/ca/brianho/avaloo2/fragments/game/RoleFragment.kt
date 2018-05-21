package ca.brianho.avaloo2.fragments.game

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo2.R
import ca.brianho.avaloo2.models.ClientSetupResponse
import ca.brianho.avaloo2.models.MessageType
import ca.brianho.avaloo2.utils.MoshiInstance
import ca.brianho.avaloo2.utils.role
import kotlinx.android.synthetic.main.fragment_role.*
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject

class RoleFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_role, container, false)
    }

    internal fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.CLIENT_SETUP.name -> handleClientSetupResponse(message)
        }
    }

    private fun handleClientSetupResponse(message: String) {
        val clientSetupResponse = MoshiInstance.fromJson<ClientSetupResponse>(message)
        role = clientSetupResponse.role
        val roleName = clientSetupResponse.role.name
        val team = clientSetupResponse.role.team
        val knowledge = clientSetupResponse.role.knowledge.joinToString()
        runOnUiThread {
            roleInfoTextView.text = getString(R.string.info_role, roleName, team, knowledge)
        }
    }
}
