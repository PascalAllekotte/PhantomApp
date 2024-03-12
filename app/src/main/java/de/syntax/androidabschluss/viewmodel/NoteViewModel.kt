package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.data.repositorys.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            NoteRepository.addNote(title, content)
        }
    }
}
