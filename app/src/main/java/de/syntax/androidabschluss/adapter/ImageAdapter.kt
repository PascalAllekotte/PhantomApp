package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.model.open.Data
import de.syntax.androidabschluss.databinding.ViewImageLayoutBinding

class ImageAdapter(
    private val onClickCallback: (position: Int, data: Data) -> Unit
) :
    ListAdapter<Data, ImageAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val viewImageLayoutBinding: ViewImageLayoutBinding) :
        RecyclerView.ViewHolder(viewImageLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewImageLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        Glide.with(holder.viewImageLayoutBinding.loadImage2)
            .load(data.url)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.viewImageLayoutBinding.loadImage2)

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickCallback(holder.adapterPosition, data)
            }
        }
    }

}
