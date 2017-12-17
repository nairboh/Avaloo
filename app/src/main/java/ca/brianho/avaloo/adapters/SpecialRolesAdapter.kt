package ca.brianho.avaloo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.Role
import ca.brianho.avaloo.models.StartGameResponse
import kotlinx.android.synthetic.main.viewholder_roles.view.*
import org.jetbrains.anko.toast

class SpecialRolesAdapter : RecyclerView.Adapter<SpecialRolesAdapter.SpecialRoleViewHolder>() {
    private val selectedRoles = mutableSetOf<String>()

    private lateinit var startGameResponse: StartGameResponse
    private var selectedNumGood = 0
    private var selectedNumEvil = 0

    private lateinit var mContext: Context

    fun initialize(startGameResponse: StartGameResponse) {
        this.startGameResponse = startGameResponse
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialRoleViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        return SpecialRoleViewHolder(layoutInflater.inflate(R.layout.viewholder_roles, parent, false))
    }

    override fun onBindViewHolder(holder: SpecialRoleViewHolder, position: Int) {
        val role = startGameResponse.roles[position]
        holder.name.text = role.name
        handleCheckBoxOnClick(holder, role)
    }

    override fun getItemCount(): Int {
        return if (::startGameResponse.isInitialized) {
            startGameResponse.roles.size
        } else {
            0
        }
    }

    private fun handleCheckBoxOnClick(holder: SpecialRoleViewHolder, role: Role) {
        holder.rolesViewHolder.setOnClickListener {
            if (holder.checkBox.isChecked) {
                holder.checkBox.isChecked = false
                handleRoleSelected(role, false)
            } else {
                val shouldAdd = when (role.team) {
                    mContext.getString(R.string.key_team_good) -> selectedNumGood < startGameResponse.numGood
                    mContext.getString(R.string.key_team_evil) -> selectedNumEvil < startGameResponse.numEvil
                    else -> throw Error("Invalid Team")
                }

                if (shouldAdd) {
                    holder.checkBox.isChecked = true
                    handleRoleSelected(role, true)
                } else {
                    mContext.toast(mContext.resources.getString(R.string.toast_max_special,
                            role.team.toLowerCase()))
                }
            }
        }
    }

    private fun handleRoleSelected(role: Role, selected: Boolean) {
        if (selected) {
            selectedRoles.add(role.name)
            when (role.team) {
                mContext.getString(R.string.key_team_good) -> selectedNumGood++
                mContext.getString(R.string.key_team_evil) -> selectedNumEvil++
            }
        } else {
            selectedRoles.remove(role.name)
            when (role.team) {
                mContext.getString(R.string.key_team_good) -> selectedNumGood--
                mContext.getString(R.string.key_team_evil) -> selectedNumEvil--
            }
        }
    }

    fun getSelectedRoles(): Set<String> = selectedRoles

    class SpecialRoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val checkBox = itemView.checkBox
        val rolesViewHolder = itemView.rolesViewHolder
    }
}