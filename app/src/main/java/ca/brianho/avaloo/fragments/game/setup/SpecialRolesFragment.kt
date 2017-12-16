package ca.brianho.avaloo.fragments.game.setup

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import org.jetbrains.anko.*
import android.support.v7.widget.LinearLayoutManager
import ca.brianho.avaloo.adapters.SpecialRolesAdapter
import ca.brianho.avaloo.models.FilteredRoleRequest
import ca.brianho.avaloo.models.Game
import ca.brianho.avaloo.models.MessageType
import ca.brianho.avaloo.utils.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_special_roles.*
import org.json.JSONObject

class SpecialRolesFragment : Fragment(), AnkoLogger {
    private lateinit var rxBusDisposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_special_roles, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
        rxBusDisposable = RxEventBus.subscribe(Consumer { handleResponseMessage(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::rxBusDisposable.isInitialized) {
            rxBusDisposable.dispose()
        }
    }

    private fun setupViewsAndListeners() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = SpecialRolesAdapter()
        submitButton.setOnClickListener {
            sendFilteredRoles()
            activity.finish()
        }
    }

    private fun handleResponseMessage(message: String) {
        when (JSONObject(message)[getString(R.string.key_type)]) {
            MessageType.START.name -> handleStartGameResponse(message)
        }
    }

    private fun handleStartGameResponse(message: String) {
        runOnUiThread {
            (recyclerView.adapter as SpecialRolesAdapter).initialize(MoshiInstance.fromJson(message))
        }
    }

    private fun sendFilteredRoles() {
        val filteredRoleSet = (recyclerView.adapter as SpecialRolesAdapter).getSelectedRoles()
        MoshiInstance.sendRequestAsJson(
            FilteredRoleRequest(gameId = Game.gameId, roles = filteredRoleSet)
        )
    }
}
