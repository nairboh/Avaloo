package ca.brianho.avaloo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.network.Player
import kotlinx.android.synthetic.main.viewholder_roles.view.*
import org.jetbrains.anko.toast

class PartyListAdapter(playerList: List<Player>, numMembers: Int) :
        RecyclerView.Adapter<PartyListAdapter.PartyPlayerViewHolder>() {
    private val mPlayerList = playerList
    private val mSelectedPlayers = mutableSetOf<Player>()

    private val mTotalMemberNum = numMembers
    private var mNumSelectedPlayers = 0

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyPlayerViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(mContext)
        return PartyPlayerViewHolder(layoutInflater.inflate(R.layout.viewholder_roles, parent, false))
    }

    override fun onBindViewHolder(holder: PartyPlayerViewHolder, position: Int) {
        val player = mPlayerList[position]
        holder.name.text = player.alias
        handleCheckBoxOnClick(holder, player)
    }

    override fun getItemCount(): Int = mPlayerList.size

    private fun handleCheckBoxOnClick(holder: PartyPlayerViewHolder, player: Player) {
        holder.rolesViewHolder.setOnClickListener {
            if (holder.checkBox.isChecked) {
                holder.checkBox.isChecked = false
                handlePlayerSelected(player, false)
            } else {
                val shouldAdd = mNumSelectedPlayers < mTotalMemberNum

                if (shouldAdd) {
                    holder.checkBox.isChecked = true
                    handlePlayerSelected(player, true)
                } else {
                    mContext.toast("MAX PLAYERS FOR PARTY")
                }
            }
        }
    }

    private fun handlePlayerSelected(player: Player, selected: Boolean) {
        if (selected) {
            mSelectedPlayers.add(player)
            mNumSelectedPlayers++
        } else {
            mSelectedPlayers.remove(player)
            mNumSelectedPlayers--
        }
    }

    fun getSelectedPlayers(): MutableSet<Player> = mSelectedPlayers

    class PartyPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val checkBox = itemView.checkBox
        val rolesViewHolder = itemView.rolesViewHolder
    }
}