package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.VokabelcardItemBinding


class VocableAdapter(private val vocabularyList: MutableList<VocabItem>) : RecyclerView.Adapter<VocableAdapter.VocabViewHolder>() {

    // Der ViewHolder für unser RecyclerView-Item
    class VocabViewHolder(val binding: VokabelcardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vocabItem: VocabItem) {
            binding.wordTextView.text = vocabItem.word
            binding.translationTextView.text = vocabItem.translation
            binding.revealButton.setOnClickListener {
                // Toggle zwischen Anzeigen und Verstecken der Lösung
                binding.loesungCard.visibility = if (binding.loesungCard.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                binding.loesung.text = vocabItem.translation // oder irgendein anderer Text, der hier erscheinen soll
            }
        }
    }

    // Diese Methode erstellt neue Views (wird vom LayoutManager aufgerufen)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        // Erstellen des ViewBindings für unser RecyclerView-Item
        val binding = VokabelcardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VocabViewHolder(binding)
    }

    // Ersetzt den Inhalt eines Views (wird vom LayoutManager aufgerufen)
    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
        holder.bind(vocabularyList[position])
    }

    // Gibt die Größe der Datenliste zurück
    override fun getItemCount() = vocabularyList.size

    // Funktion, um Items zu entfernen (nützlich für Swipe-Aktionen)
    fun removeAt(position: Int) {
        vocabularyList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateList(newItems: List<VocabItem>) {
        vocabularyList.clear()
        vocabularyList.addAll(newItems)
        notifyDataSetChanged()
    }
}
