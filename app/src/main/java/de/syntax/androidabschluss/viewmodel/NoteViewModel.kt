package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.adapter.local.getDatabaseNote
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.data.repositorys.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application){

    private val dataBase = getDatabaseNote(application)
    private val repository = NoteRepository(dataBase)

    val noteList = repository.noteListe


    private val _noteComplete = MutableLiveData<Boolean>()
    val noteComplete: LiveData<Boolean>
        get() = _noteComplete

    fun insertNoteItem(noteItem: NoteItem){
        viewModelScope.launch {
            repository.insert(noteItem)
            _noteComplete.value = true
        }
    }
    fun delete(noteItem: NoteItem){
        viewModelScope.launch {
            repository.delete(noteItem)
            _noteComplete.value = true
        }
    }

    fun updateNoteItem(noteItem: NoteItem){
        viewModelScope.launch {
            repository.update(noteItem)
            _noteComplete.value = true
        }
    }

    fun insertNoteItemList(noteItemList: List<NoteItem>){
        viewModelScope.launch {
            repository.insertAll(noteItemList)
            _noteComplete.value = true
        }
    }


}
