package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.adapter.local.getDatabase
import de.syntax.androidabschluss.data.model.open.VocabItem
import de.syntax.androidabschluss.data.repositorys.VokabelRepository
import kotlinx.coroutines.launch

class VokabelViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = VokabelRepository(database)

    val vokabelList = repository.vokabelListe
    val uniqueBlockList = repository.uniqueBlockList


    private val _complete = MutableLiveData<Boolean>()
    val complete: LiveData<Boolean>
        get() = _complete

    fun getVocabItemsByBlock(blockName: String): LiveData<List<VocabItem>> {
        return repository.getVocabItemsByBlock(blockName)
    }
    fun insertVocabItem(vocabItem: VocabItem) {
        viewModelScope.launch {
            repository.insert(vocabItem)
            _complete.value = true
        }
    }

    fun updateVocabItem(vocabItem: VocabItem) {
        viewModelScope.launch {
            repository.update(vocabItem)
            _complete.value = true
        }
    }

    fun insertVocabItemList(vocabItemList: List<VocabItem>) {
        viewModelScope.launch {
            repository.insertAll(vocabItemList)
            _complete.value = true
        }
    }

    fun unsetOperationComplete() {
        _complete.value = false
    }


}
