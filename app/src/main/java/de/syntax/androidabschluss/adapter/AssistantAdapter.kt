package de.syntax.androidabschluss.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.databinding.BotviewLayoutBinding

class AssistantAdapter : ListAdapter<Assistant, AssistantAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Assistant>() {
        override fun areItemsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
            return oldItem.assistantId == newItem.assistantId
        }

        override fun areContentsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(private val botviewLayoutBinding: BotviewLayoutBinding)
        : RecyclerView.ViewHolder(botviewLayoutBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
