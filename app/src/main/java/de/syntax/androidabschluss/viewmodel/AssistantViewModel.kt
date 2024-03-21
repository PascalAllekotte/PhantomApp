package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.data.repositorys.AssistantRepository

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val assistantRepository = AssistantRepository(application)

    val assistantStatusFlow get() = assistantRepository.assistantStateFlow
    val statusLiveData get() = assistantRepository.statusLiveData


    fun getAssistantList(){
        assistantRepository.getAssistantList()
    }

    fun insertAssistant(assistant: Assistant){
        assistantRepository.insertAssistant(assistant)
    }

    fun deleteAssistantUsingId(assistantId: String){
        assistantRepository.deleteAssistantUsingId(assistantId)

    }

    fun updateAssistant(assistant: Assistant){
        assistantRepository.updateAssistant(assistant)
    }
}