package ca.brianho.avaloo.fragments.game

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.network.ClientSetupResponse
import ca.brianho.avaloo.network.WSInterface
import ca.brianho.avaloo.utils.*
import kotlinx.android.synthetic.main.fragment_role.*

class RoleFragment : Fragment(), WSInterface {
    private var message = ""

    override fun handleResponseMessage(message: String?) {
        this.message = message!!
        boardFragment = BoardFragment()
        wsListener.setInterface(boardFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_role, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clientSetupResponse = moshi.fromJson<ClientSetupResponse>(message)
        playerList = clientSetupResponse.playerList
        val roleName = clientSetupResponse.role.name
        val team = clientSetupResponse.role.team
        val teammates = clientSetupResponse.role.knowledge.joinToString()
        textView.text = getString(R.string.role_info, roleName, team, teammates)
    }
}
