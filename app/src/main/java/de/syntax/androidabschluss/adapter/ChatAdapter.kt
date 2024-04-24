package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.Chat
import de.syntax.androidabschluss.databinding.ReceiverItemBinding
import de.syntax.androidabschluss.databinding.SenderItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(
    private val onClickCallback: (message : String, view: View) -> Unit
) :
ListAdapter<Chat, RecyclerView.ViewHolder>(DiffCallback()){

class SenderViewHolder(private val senderItemBinding: SenderItemBinding):
    RecyclerView.ViewHolder(senderItemBinding.root){

        // nachrichten binden
        fun bind(chat : Chat){
            senderItemBinding.txtMessage.text = chat.message.content
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
            senderItemBinding.txtDate.text = dateFormat.format(chat.date)
        }
    }


    class ReceiverViewHolder(private val receiverItemBinding: ReceiverItemBinding):
        RecyclerView.ViewHolder(receiverItemBinding.root){
        // nachrichten binden
        fun bind(chat : Chat) {
            if (chat.message.content.isNotEmpty()) {
                receiverItemBinding.txtMessage.text = chat.message.content
                val dateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
                receiverItemBinding.txtDate.text = dateFormat.format(chat.date)

                receiverItemBinding.typingLAV.visibility = View.GONE
                receiverItemBinding.txtDate.visibility = View.VISIBLE
                receiverItemBinding.txtMessage.visibility = View.VISIBLE
            } else {

                receiverItemBinding.typingLAV.visibility = View.VISIBLE
                receiverItemBinding.txtDate.visibility = View.GONE
                receiverItemBinding.txtMessage.visibility = View.GONE
            }
        }
    }


    // Erstellt neue ViewHolder empfänger oder sender
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            ReceiverViewHolder(
                ReceiverItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else (
                SenderViewHolder(
                    SenderItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        )
    }


    // bindet neuen ViewHolder empfänger oder sender
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)
        if (chat.message.role == "user"){
            (holder as SenderViewHolder).bind(chat)
        }else{
            (holder as ReceiverViewHolder).bind(chat)

        }
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickCallback(chat.message.content, holder.itemView)
            }
            true
        }
    }


    // Bestimmt den Typ der View
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).message.role == "user"){
            0 // senderitem
        }else{
            1 // receiveritem
        }
    }



    // Hilfsklasse zur Optimierung von Listenoperationen durch Vergleich der Elemente.
    class DiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.chatId == newItem.chatId
        }
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem

        }
    }
}