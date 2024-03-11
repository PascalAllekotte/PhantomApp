package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.databinding.LearningItemBinding


class LearningAdapter(
    private val vocabularyList: MutableList<VocabItem>,
    private val dao: VokabelDataBaseDao,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<LearningAdapter.LearnViewHolder>() {



    class LearnViewHolder(val binding: LearningItemBinding, private val dao: VokabelDataBaseDao) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vocabItem: VocabItem) {

            binding.blockname.text = vocabItem.block

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnViewHolder {
        val binding = LearningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnViewHolder(binding, dao)
    }

    override fun onBindViewHolder(holder: LearnViewHolder, position: Int) {
        holder.bind(vocabularyList[position])

        holder.binding.blockCardView.setOnClickListener{


        }
    }



    override fun getItemCount() = vocabularyList.size

    // Function to remove items (useful for swipe actions)
    fun removeAt(position: Int) {
        vocabularyList.removeAt(position)
        notifyItemRemoved(position)
    }



    // Function to update the list of items
    fun updateList(newItems: LiveData<List<VocabItem>>) {

        newItems.observe(lifecycleOwner, Observer { items ->
            vocabularyList.clear()
            vocabularyList.addAll(items)
            notifyDataSetChanged()

    })


    }



}
