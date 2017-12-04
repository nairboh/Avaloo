package ca.brianho.avaloo.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.network.Player
import kotlinx.android.synthetic.main.viewholder_player.view.*
import org.json.JSONObject
import java.util.*

class PlayerListAdapter : RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {
    private val mItems: MutableList<Player>
    private val mItemTouchHelper: ItemTouchHelper

    init {
        mItems = mutableListOf()
        mItemTouchHelper = ItemTouchHelper(
            object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

                override fun onMove(recyclerView: RecyclerView?,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition
                    if (fromPosition < toPosition) {
                        for (i in fromPosition until toPosition) {
                            Collections.swap(mItems, i, i + 1)
                        }
                    } else {
                        for (i in fromPosition downTo toPosition + 1) {
                            Collections.swap(mItems, i, i - 1)
                        }
                    }
                    notifyItemMoved(fromPosition, toPosition)
                    return true
                }
            }
        )

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlayerViewHolder(layoutInflater.inflate(R.layout.viewholder_player, parent, false))
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.alias.text = mItems[position].alias
    }

    override fun getItemCount(): Int = mItems.size

    fun add(playerJson: JSONObject) {
        val positionToInsert = itemCount
        val alias = playerJson["alias"] as String
        val playerId = playerJson["playerId"] as String
        mItems.add(Player(alias, playerId))
        notifyItemInserted(positionToInsert)
    }

    fun getItems(): MutableList<Player> = mItems

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alias = itemView.alias
    }
}