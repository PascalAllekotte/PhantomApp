package de.syntax.androidabschluss.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.databinding.BotviewLayoutBinding
import de.syntax.androidabschluss.utils.assistantImageList

class AssistantAdapter(
    private val onClickDeleteUpdateCallback: (type: String, position: Int, assistant: Assistant) -> Unit
) : ListAdapter<Assistant, AssistantAdapter.ViewHolder>(DiffCallback()) {

    private var currentStrokeColor: Int? = null

    // stroke farbe
    fun updateStrokeColor(color: Int) {
        currentStrokeColor = color
        notifyDataSetChanged()
    }

    // implementierung der DiffUtil.ItemCallback um zu aktuallisieren
    class DiffCallback : DiffUtil.ItemCallback<Assistant>() {
        override fun areItemsTheSame(oldItem: Assistant, newItem: Assistant): Boolean = oldItem.assistantId == newItem.assistantId
        override fun areContentsTheSame(oldItem: Assistant, newItem: Assistant): Boolean = oldItem == newItem
    }


    class ViewHolder(val binding: BotviewLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(BotviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val assistant = getItem(position)

        currentStrokeColor?.let { color ->
            val colorStateList = ColorStateList.valueOf(color)
            holder.binding.botCard.setStrokeColor(colorStateList)
        }

        holder.binding.botTitle.text = assistant.assistantName
        holder.binding.botImage.setImageResource(assistantImageList[assistant.assistantImg])

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onClickDeleteUpdateCallback("click", holder.adapterPosition, assistant)
            }
        }
        holder.binding.editview.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onClickDeleteUpdateCallback("delete", holder.adapterPosition, assistant)
            }
        }
        holder.binding.deleteview.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                onClickDeleteUpdateCallback("update", holder.adapterPosition, assistant)
            }
        }
    }
}
