package de.syntax.androidabschluss.data.repositorys

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.adapter.local.ChatGPTDatabase
import de.syntax.androidabschluss.adapter.local.Resource
import de.syntax.androidabschluss.data.model.open.Assistant
import de.syntax.androidabschluss.utils.StatusResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AssistantRepository(application: Application) {
    private val assistantDao = ChatGPTDatabase.getInstance(application).assistantDao

    private val _statusLiveData = MutableLiveData<Resource<StatusResult>>()
    val statusLiveData : LiveData<Resource<StatusResult>>
        get() = _statusLiveData

    fun insertAssistant(assistant: Assistant){
        try {
            _statusLiveData.postValue(Resource.Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = assistantDao.insertAssistant(assistant)
                handleResult(result.toInt(),"Insert Assistant geklappt", StatusResult.Added)
            }
        }catch (e:Exception){
            _statusLiveData.postValue(Resource.Error(e.message.toString()))
        }

    }

    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {
    if (result != -1){
        _statusLiveData.postValue(Resource.Success(statusResult,message))
    }else{
        _statusLiveData.postValue(Resource.Error("Something ist schief gelaufen"))

    }

    }

}