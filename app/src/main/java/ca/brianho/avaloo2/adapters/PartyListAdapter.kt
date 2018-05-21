package ca.brianho.avaloo2.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo2.R
import ca.brianho.avaloo2.models.Player
import kotlinx.android.synthetic.main.viewholder_roles.view.*
import org.jetbrains.anko.toast

class PartyListAdapter : RecyclerView.Adapter<PartyListAdapter.PartyPlayerViewHolder>() {
    private lateinit var mPlayerList: List<Player>
    private val mSelectedPlayers = mutableSetOf<Player>()

    private var mTotalMemberNum: Int = 0
    private var mNumSelectedPlayers = 0

    private lateinit var mContext: Context

    fun setPlayerList(playerList: List<Player>) {
        mPlayerList = playerList
    }

    fun setNumMembers(numMembers: Int) {
        mTotalMemberNum = numMembers
    }

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

    override fun getItemCount(): Int = if (::mPlayerList.isInitialized) mPlayerList.size else 0

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
                    mContext.toast(mContext.getString(R.string.toast_max_players))
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

    fun getSelectedPlayers(): Set<Player> = mSelectedPlayers

    fun getAllowedSelectedPlayers(): Int {
        return mTotalMemberNum
    }

    class PartyPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val checkBox = itemView.checkBox
        val rolesViewHolder = itemView.rolesViewHolder
    }
}