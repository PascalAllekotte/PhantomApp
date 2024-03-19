package de.syntax.androidabschluss.data.repositorys

import android.app.Application
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.adapter.local.ChatGPTDatabase
import de.syntax.androidabschluss.adapter.local.Resource
import de.syntax.androidabschluss.utils.StatusResult

class AssistantRepository(application: Application) {
    private val assistantDao = ChatGPTDatabase.getInstance(application).assistantDao

    piovate val _statusLiveData = MutableLiveData<Resource<StatusResult>>
}