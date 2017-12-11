package ca.brianho.avaloo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.network.Role
import kotlinx.android.synthetic.main.viewholder_roles.view.*
import org.jetbrains.anko.toast

class SpecialRolesAdapter(rolesList: MutableList<Role>, numGood: Int, numEvil: Int) :
        RecyclerView.Adapter<SpecialRolesAdapter.SpecialRoleViewHolder>() {
    private val mRolesList = rolesList
    private val mSelectedRoles = mutableSetOf<String>()

    private val mTotalNumGood = numGood
    private val mTotalNumEvil = numEvil

    private var mNumGood = 0
    private var mNumEvil = 0

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialRoleViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        return SpecialRoleViewHolder(layoutInflater.inflate(R.layout.viewholder_roles, parent, false))
    }

    override fun onBindViewHolder(holder: SpecialRoleViewHolder, position: Int) {
        val role = mRolesList[position]
        holder.name.text = role.name
        handleCheckBoxOnClick(holder, role)
    }

    override fun getItemCount(): Int = mRolesList.size

    private fun handleCheckBoxOnClick(holder: SpecialRoleViewHolder, role: Role) {
        holder.rolesViewHolder.setOnClickListener {
            if (holder.checkBox.isChecked) {
                holder.checkBox.isChecked = false
                handleRoleSelected(role, false)
            } else {
                val shouldAdd = when (role.team) {
                    mContext.getString(R.string.key_team_good) -> mNumGood < mTotalNumGood
                    mContext.getString(R.string.key_team_evil) -> mNumEvil < mTotalNumEvil
                    else -> throw Error(mContext.getString(R.string.error_invalid_team))
                }

                if (shouldAdd) {
                    holder.checkBox.isChecked = true
                    handleRoleSelected(role, true)
                } else {
                    mContext.toast(mContext.resources.getString(R.string.max_special_roles_selected,
                            role.team.toLowerCase()))
                }
            }
        }
    }

    private fun handleRoleSelected(role: Role, selected: Boolean) {
        if (selected) {
            mSelectedRoles.add(role.name)
            when (role.team) {
                mContext.getString(R.string.key_team_good) -> mNumGood++
                mContext.getString(R.string.key_team_evil) -> mNumEvil++
            }
        } else {
            mSelectedRoles.remove(role.name)
            when (role.team) {
                mContext.getString(R.string.key_team_good) -> mNumGood--
                mContext.getString(R.string.key_team_evil) -> mNumEvil--
            }
        }
    }

    fun getSelectedRoles(): MutableSet<String> = mSelectedRoles

    class SpecialRoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val checkBox = itemView.checkBox
        val rolesViewHolder = itemView.rolesViewHolder
    }
}