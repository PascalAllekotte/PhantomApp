package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.adapter.local.getDatabaseTerm
import de.syntax.androidabschluss.data.model.open.TermItem
import de.syntax.androidabschluss.data.repositorys.TermRepository
import kotlinx.coroutines.launch

class TermViewModel (application: Application): AndroidViewModel(application){

    private val database = getDatabaseTerm(application)
    private val repository = TermRepository(database)

    val termList = repository.termLsite

    private val _termComplete = MutableLiveData<Boolean>()
    val termComplete: LiveData<Boolean>
        get() = _termComplete

    fun insertTermItem(termItem: TermItem){
        viewModelScope.launch {
            repository.insert(termItem)
            _termComplete.value = true
        }
    }

    fun deleteTermItem(termItem: TermItem){
        viewModelScope.launch {
            repository.delete(termItem)
            _termComplete.value = true
        }
    }
    fun updateTermItem(termItem: TermItem){
        viewModelScope.launch {
            repository.update(termItem)
            _termComplete.value = true
        }
    }






}