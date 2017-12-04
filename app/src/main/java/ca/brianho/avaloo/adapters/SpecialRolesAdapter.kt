package ca.brianho.avaloo.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import kotlinx.android.synthetic.main.viewholder_roles.view.*

class SpecialRolesAdapter(rolesList: MutableList<String>) :
        RecyclerView.Adapter<SpecialRolesAdapter.SpecialRoleViewHolder>() {
    private val mRolesList = rolesList
    private val mSelectedRoles = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialRoleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SpecialRoleViewHolder(layoutInflater.inflate(R.layout.viewholder_roles, parent, false))
    }

    override fun onBindViewHolder(holder: SpecialRoleViewHolder, position: Int) {
        val roleName = mRolesList[position]
        holder.name.text = roleName
        handleCheckBoxOnClick(holder, roleName)
    }

    override fun getItemCount(): Int = mRolesList.size

    private fun handleCheckBoxOnClick(holder: SpecialRoleViewHolder, roleName: String) {
        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                mSelectedRoles.add(roleName)
            } else {
                mSelectedRoles.remove(roleName)
            }
        }
    }

    fun getSelectedRoles(): MutableSet<String> = mSelectedRoles

    class SpecialRoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val checkBox = itemView.checkBox
    }
}