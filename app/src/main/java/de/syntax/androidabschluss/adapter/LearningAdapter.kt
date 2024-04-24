package de.syntax.androidabschluss.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.databinding.LearningItemBinding


class LearningAdapter(
    private val blockList: MutableList<String>,
    private val lifecycleOwner: LifecycleOwner,
    private val onItemCklicked: (String) -> Unit

) : RecyclerView.Adapter<LearningAdapter.LearnViewHolder>() {


    private var currentStrokeColor: Int? = null

    fun updateStrokeColor(color: Int) {
        currentStrokeColor = color
        notifyDataSetChanged()
    }

    class LearnViewHolder(val binding: LearningItemBinding, private val onItemCklicked: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blockName: String, currentStrokeColor: Int?) {
            binding.blockname.text = blockName

            binding.blockCardView.setOnClickListener{
            onItemCklicked(blockName)
            }

            currentStrokeColor?.let { color ->
                val colorStateList = ColorStateList.valueOf(color)
                binding.blockCardView.setStrokeColor(colorStateList)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnViewHolder {
        val binding = LearningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnViewHolder(binding, onItemCklicked)
    }

    override fun onBindViewHolder(holder: LearnViewHolder, position: Int) {
        holder.bind(blockList[position], currentStrokeColor)
    }

    override fun getItemCount() = blockList.size

    // Funktion zum Aktualisieren der Liste
    fun updateList(newItems: List<String>) {
        blockList.clear()
        blockList.addAll(newItems)
        notifyDataSetChanged()
    }
}
