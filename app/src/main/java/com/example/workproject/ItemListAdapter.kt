package com.example.workproject

import com.example.workproject.database.ActivityTable


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workproject.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClicked: (ActivityTable) -> Unit):
    ListAdapter<ActivityTable, ItemListAdapter.ItemViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: ActivityTable) {
            binding.itemActivity.text = activity.activityType
            binding.itemType.text = activity.type
            binding.itemParticipants.text = activity.participants.toString()
            binding.itemPrice.text = activity.price.toString()

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ActivityTable>() {
            override fun areItemsTheSame(oldItem: ActivityTable, newItem: ActivityTable): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ActivityTable, newItem: ActivityTable): Boolean {
                return oldItem.activityType == newItem.activityType
            }
        }
    }
}