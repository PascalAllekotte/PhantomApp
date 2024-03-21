package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.databinding.BotviewLayoutBinding
import de.syntax.androidabschluss.utils.assistantImageList

class AssistantAdapter(
    private val onClickdeleteUpdateCallback : (type: String, position: Int, assistant: Assistant) -> Unit

) : ListAdapter<Assistant, AssistantAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Assistant>() {
        override fun areItemsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
            return oldItem.assistantId == newItem.assistantId
        }

        override fun areContentsTheSame(oldItem: Assistant, newItem: Assistant): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val botviewLayoutBinding: BotviewLayoutBinding)
        : RecyclerView.ViewHolder(botviewLayoutBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BotviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val assistant = getItem(position)
        holder.botviewLayoutBinding.botTitle.text = assistant.assistantName
        holder.botviewLayoutBinding.botImage.setImageResource(assistantImageList[assistant.assistantImg])

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickdeleteUpdateCallback("click", holder.adapterPosition, assistant)            }
        }
        holder.botviewLayoutBinding.editview.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickdeleteUpdateCallback("delete", holder.adapterPosition, assistant)            }
        }
        holder.botviewLayoutBinding.deleteview.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickdeleteUpdateCallback("update", holder.adapterPosition, assistant)            }
        }

    }

}
