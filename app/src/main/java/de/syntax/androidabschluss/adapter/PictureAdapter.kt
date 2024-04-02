package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.model.open.PictureItem
import de.syntax.androidabschluss.databinding.ViewImageLayoutBinding

class PictureAdapter(
    private val onClickCallback: (position: Int, pictureItem: PictureItem) -> Unit,
    private val onLongClickCallback: (position: Int, pictureItem: PictureItem) -> Unit
) : ListAdapter<PictureItem, PictureAdapter.ViewHolder>(DiffCallback()) {

    // Mit DiffCallback erkennen wir Änderungen in der Liste effizient
    class DiffCallback : DiffUtil.ItemCallback<PictureItem>() {
        override fun areItemsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            // Vergleicht, ob zwei Bildobjekte dieselbe ID haben
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PictureItem, newItem: PictureItem): Boolean {
            // Überprüft, ob der Inhalt von zwei Bildobjekten identisch ist
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ViewImageLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pictureItem: PictureItem, onClick: (position: Int, pictureItem: PictureItem) -> Unit,
                 onLongClick: (position: Int, pictureItem: PictureItem) -> Unit) {
            // Hier nutzen wir Glide, um das Bild zu laden und anzuzeigen
            Glide.with(binding.loadImage2)
                .load(pictureItem.url)
                .placeholder(R.drawable.ic_placeholder) // Zeigt ein Standardbild, während das eigentliche Bild lädt
                .into(binding.loadImage2)

            // Click-Listener, um auf Klicks auf das Bild zu reagieren
            itemView.setOnClickListener {
                onClick(adapterPosition, pictureItem)
            }

            // LongClick-Listener, um auf lange Klicks zu reagieren
            itemView.setOnLongClickListener {
                onLongClick(adapterPosition, pictureItem)
                true // Gibt true zurück, da das item hier behandelt wird
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

    // Verknüpft ein Bild-Item mit einem ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Holt das aktuelle Bild-Item basierend auf der Position
        val pictureItem = getItem(position)
        // Bindet das Bild-Item und seine Daten an den ViewHolder
        holder.bind(pictureItem, onClickCallback, onLongClickCallback)
    }
}
