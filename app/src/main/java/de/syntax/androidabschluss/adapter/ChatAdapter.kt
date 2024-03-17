package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.open.Chat
import de.syntax.androidabschluss.databinding.ReceiverItemBinding
import de.syntax.androidabschluss.databinding.SenderItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter :
ListAdapter<Chat, RecyclerView.ViewHolder>(DiffCallback()){

class SenderViewHolder(private val senderItemBinding: SenderItemBinding):
    RecyclerView.ViewHolder(senderItemBinding.root){

        fun bind(chat : Chat){

            senderItemBinding.txtMessage.text = chat.message
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm a", Locale.getDefault())
            senderItemBinding.txtDate.text = dateFormat.format(chat.date)

        }

    }

    class ReceiverViewHolder(private val receiverItemBinding: ReceiverItemBinding):
        RecyclerView.ViewHolder(receiverItemBinding.root){

        fun bind(chat : Chat){

            receiverItemBinding.txtMessage.text = chat.message
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm a", Locale.getDefault())
            receiverItemBinding.txtDate.text = dateFormat.format(chat.date)

        }

    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)

        if (chat.messageType.equals("sender", true)){
            (holder as SenderViewHolder).bind(chat)
        }else{
            (holder as ReceiverViewHolder).bind(chat)

        }

    }


    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).messageType.equals("sender", true)){
            0 // senderitem
        }else{
            1 // receiveritem
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem

        }
    }



}