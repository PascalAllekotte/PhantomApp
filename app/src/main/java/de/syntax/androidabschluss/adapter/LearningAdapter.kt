package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.LearningItemBinding


class LearningAdapter(
    private val blockList: MutableList<String>,
    private val lifecycleOwner: LifecycleOwner,
    private val onItemCklicked: (String) -> Unit

) : RecyclerView.Adapter<LearningAdapter.LearnViewHolder>() {

    class LearnViewHolder(val binding: LearningItemBinding, private val onItemCklicked: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blockName: String) {
            binding.blockname.text = blockName

            binding.blockCardView.setOnClickListener{

            onItemCklicked(blockName)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnViewHolder {
        val binding = LearningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnViewHolder(binding, onItemCklicked)
    }

    override fun onBindViewHolder(holder: LearnViewHolder, position: Int) {
        holder.bind(blockList[position])
    }

    override fun getItemCount() = blockList.size

    // Funktion zum Aktualisieren der Liste
    fun updateList(newItems: List<String>) {
        blockList.clear()
        blockList.addAll(newItems)
        notifyDataSetChanged()
    }
}
