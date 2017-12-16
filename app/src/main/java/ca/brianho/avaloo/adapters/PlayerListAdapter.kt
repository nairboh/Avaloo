package ca.brianho.avaloo.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.brianho.avaloo.R
import ca.brianho.avaloo.models.Player
import kotlinx.android.synthetic.main.viewholder_player.view.*
import java.util.*

class PlayerListAdapter(player: Player) :
        RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {
    private val mItems: MutableList<Player> = mutableListOf(player)
    private val mItemTouchHelper: ItemTouchHelper

    private lateinit var context: Context

    init {
        mItemTouchHelper = ItemTouchHelper(
            object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

                override fun onMove(recyclerView: RecyclerView?,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition = target.adapterPosition

                    setViewHolderText(viewHolder, toPosition, mItems[fromPosition].alias)
                    setViewHolderText(target, fromPosition, mItems[toPosition].alias)

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

                override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
                    super.clearView(recyclerView, viewHolder)
                    notifyDataSetChanged()
                }
            }
        )

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        return PlayerViewHolder(layoutInflater.inflate(R.layout.viewholder_player, parent, false))
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        setViewHolderText(holder, position, mItems[position].alias)
    }

    override fun getItemCount(): Int = mItems.size

    fun add(player: Player) {
        val positionToInsert = itemCount
        mItems.add(player)
        notifyItemInserted(positionToInsert)
    }

    fun getItems(): MutableList<Player> = mItems

    private fun setViewHolderText(holder: RecyclerView.ViewHolder, position: Int, alias: String) {
        (holder as PlayerViewHolder).alias.text =
                context.getString(R.string.player_order, position + 1, alias)
    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alias = itemView.alias
    }
}