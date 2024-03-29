package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.model.open.PictureItem // Angenommen, dies ist Ihre Entity-Klasse
import de.syntax.androidabschluss.databinding.ViewImageLayoutBinding

class PictureAdapter(
    private val onClickCallback: (position: Int, pictureItem: PictureItem) -> Unit
) : ListAdapter<PictureItem, PictureAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<PictureItem>() {
        override fun areItemsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            // Möglicherweise müssen Sie diese Methode basierend auf Ihrer PictureItem-Klasse anpassen
            return oldItem.id == newItem.id // Annahme, dass PictureItem eine ID hat.
        }

        override fun areContentsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ViewImageLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pictureItem: PictureItem, onClick: (position: Int, pictureItem: PictureItem) -> Unit) {
            Glide.with(binding.loadImage2)
                .load(pictureItem.url) // Annahme, dass PictureItem eine URL-Variable hat
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.loadImage2)

            itemView.setOnClickListener {
                onClick(adapterPosition, pictureItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewImageLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictureItem = getItem(position)
        holder.bind(pictureItem, onClickCallback)
    }
}
