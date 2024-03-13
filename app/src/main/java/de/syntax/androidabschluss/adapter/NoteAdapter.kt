package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.databinding.NoteItemBinding

class NoteAdapter(
    private val noteList : MutableList<NoteItem>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    class NoteViewHolder(
        val binding: NoteItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(noteItem: NoteItem){
            binding.noteTitle.text = noteItem.title
            binding.noteContent.text = noteItem.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)

    }

    override fun getItemCount() = noteList.size


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    holder.bind(noteList[position])
    }

}