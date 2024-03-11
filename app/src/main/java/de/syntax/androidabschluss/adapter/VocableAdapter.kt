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

class VocableAdapter(
    private val vocabularyList: MutableList<VocabItem>,
    private val onItemChanged: (VocabItem) -> Unit
) : RecyclerView.Adapter<VocableAdapter.VocabViewHolder>() {

    class VocabViewHolder(
        val binding: VokabelcardItemBinding,
        private val onItemChanged: (VocabItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vocabItem: VocabItem) {
            binding.language.text = vocabItem.language
            binding.vokabel.text = vocabItem.translation
            binding.cbFavorite.isChecked = vocabItem.favorite

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
                    onItemChanged(vocabItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        val binding = VokabelcardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VocabViewHolder(binding, onItemChanged)
    }

    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
        holder.bind(vocabularyList[position])
    }

    override fun getItemCount() = vocabularyList.size

    fun updateList(newItems: List<VocabItem>) {
        vocabularyList.clear()
        vocabularyList.addAll(newItems)
        notifyDataSetChanged()
    }
}



