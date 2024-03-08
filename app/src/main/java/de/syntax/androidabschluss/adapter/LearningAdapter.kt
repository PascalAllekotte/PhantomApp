package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.LearningItemBinding
import de.syntax.androidabschluss.databinding.VokabelcardItemBinding


class LearningAdapter(private val vocabularyList: MutableList<VocabItem>, private val dao: VokabelDataBaseDao) : RecyclerView.Adapter<LearningAdapter.LearnViewHolder>() {



    // Corrected the ViewHolder for our RecyclerView item
    class LearnViewHolder(val binding: LearningItemBinding, private val dao: VokabelDataBaseDao) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vocabItem: VocabItem) {

            binding.blockname.text = vocabItem.block





        }
    }

    // This method creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnViewHolder {
        // Create the ViewBinding for our RecyclerView item
        val binding = LearningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnViewHolder(binding, dao)
    }

    // Replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: LearnViewHolder, position: Int) {
        holder.bind(vocabularyList[position])
    }

    // Returns the size of the data list
    override fun getItemCount() = vocabularyList.size

    // Function to remove items (useful for swipe actions)
    fun removeAt(position: Int) {
        vocabularyList.removeAt(position)
        notifyItemRemoved(position)
    }

    // Function to update the list of items
    fun updateList(newItems: List<VocabItem>) {
        vocabularyList.clear()
        vocabularyList.addAll(newItems)
        notifyDataSetChanged()

    }
}
