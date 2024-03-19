package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.data.repositorys.AssistantRepository

class AssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val assistantRepository = AssistantRepository(application)
    val statusLiveData get() = assistantRepository.statusLiveData

    fun insertRobot(assistant: Assistant){
        assistantRepository.insertAssistant(assistant)
    }


}