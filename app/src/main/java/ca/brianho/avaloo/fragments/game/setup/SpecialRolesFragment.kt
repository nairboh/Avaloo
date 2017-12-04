package ca.brianho.avaloo.fragments.game.setup

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.utils.websocket
import org.jetbrains.anko.*
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import ca.brianho.avaloo.adapters.SpecialRolesAdapter
import ca.brianho.avaloo.utils.gameId
import ca.brianho.avaloo.utils.roles
import kotlinx.android.synthetic.main.fragment_special_roles.*
import org.json.JSONArray
import org.json.JSONObject

class SpecialRolesFragment : Fragment(), AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_special_roles, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = SpecialRolesAdapter(roles)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        submitButton.setOnClickListener{ sendFilteredRoles() }
    }

    private fun sendFilteredRoles() {
        val filteredRoleSet = (recyclerView.adapter as SpecialRolesAdapter).getSelectedRoles()
        val jsonArray = JSONArray(filteredRoleSet)
        val json = JSONObject()
        json.put("type", "FILTERED_ROLES")
        json.put("roles", jsonArray)
        json.put("gameId", gameId)
        Log.e("JSON STRING", json.toString())

        websocket.send(json.toString())
    }
}
