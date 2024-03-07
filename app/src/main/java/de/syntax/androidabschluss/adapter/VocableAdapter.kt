package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.VokabelcardItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VocableAdapter(private val vocabularyList: MutableList<VocabItem>, private val dao: VokabelDataBaseDao) : RecyclerView.Adapter<VocableAdapter.VocabViewHolder>() {



    // Corrected the ViewHolder for our RecyclerView item
    class VocabViewHolder(val binding: VokabelcardItemBinding, private val dao: VokabelDataBaseDao) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vocabItem: VocabItem) {
            // Set the text for the first language and its translation
            binding.language.text = vocabItem.language
            binding.vokabel.text = vocabItem.translation
            binding.cbFavorite.isChecked = vocabItem.favorite




            // When the reveal button is clicked, show or hide the second language and its translation
            binding.revealButton.setOnClickListener {
                if (binding.loesungCard.visibility == View.VISIBLE) {
                    binding.loesungCard.visibility = View.GONE
                } else {
                    binding.loesungCard.visibility = View.VISIBLE
                    binding.language2.text = vocabItem.language2
                    binding.vokabel2.text = vocabItem.translation2
                }
            }

            binding.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
                if (vocabItem.favorite != isChecked) {
                    vocabItem.favorite = isChecked
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.update(vocabItem) // Aktualisieren des Objekts in der Datenbank
                    }
                }
            }
        }
    }

    // This method creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        // Create the ViewBinding for our RecyclerView item
        val binding = VokabelcardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VocabViewHolder(binding, dao)
    }

    // Replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
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
