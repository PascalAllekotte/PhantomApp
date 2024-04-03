package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.databinding.NewnoteItemBinding

class NoteAdapter(
    private val noteList: MutableList<NoteItem>,
    private val onNoteDeleted: (NoteItem) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: NewnoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(noteItem: NoteItem, onDeleteClick: (NoteItem) -> Unit) {
            binding.tvTitle.text = noteItem.title
            binding.tvDesc.text = noteItem.content
            binding.tvDateTime.setOnClickListener { onDeleteClick(noteItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NewnoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val noteItem = noteList[position]
        holder.bind(noteItem, onNoteDeleted)
    }

    fun updateList(newNoteItems: List<NoteItem>) {
        noteList.clear()
        noteList.addAll(newNoteItems)
        notifyDataSetChanged()
    }
}
