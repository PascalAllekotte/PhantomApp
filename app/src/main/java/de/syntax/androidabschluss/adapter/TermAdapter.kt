package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.TermItem
import de.syntax.androidabschluss.databinding.TermItemBinding

class TermAdapter(
    private val termList: MutableList<TermItem>,
    private val onTermDeleted: (TermItem) -> Unit
) : RecyclerView.Adapter<TermAdapter.TermViewHolder>(){



    class TermViewHolder(val binding : TermItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(termItem: TermItem, onTermDeleted: (TermItem) -> Unit){
            binding.termtext.text = termItem.termText
            binding.termdate.text = termItem.termDate


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val binding = TermItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TermViewHolder(binding)
    }

    override fun getItemCount(): Int = termList.size

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val termItem = termList[position]
        holder.bind(termItem,onTermDeleted)
    }

    // toDo später für strokes
    fun updateList(termItem: List<TermItem>){
        termList.clear()
        termList.addAll(termItem)
        notifyDataSetChanged()


    }
}