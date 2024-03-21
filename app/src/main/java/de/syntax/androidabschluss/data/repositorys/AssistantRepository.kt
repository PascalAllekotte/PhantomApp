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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AssistantRepository(application: Application) {
    private val assistantDao = ChatGPTDatabase.getInstance(application).assistantDao

    private val _assistantStateFlow = MutableStateFlow<Resource<Flow<List<Assistant>>>>(Resource.Loading())
    val assistantStateFlow : StateFlow<Resource<Flow<List<Assistant>>>>
        get() = _assistantStateFlow



    private val _statusLiveData = MutableLiveData<Resource<StatusResult>?>()
    val statusLiveData : LiveData<Resource<StatusResult>?>
        get() = _statusLiveData


    fun clearStatusLiveData(){
        _statusLiveData.value = null

    }

    fun getAssistantList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _assistantStateFlow.emit(Resource.Loading())
                val result = assistantDao.getAssistantList()
                _assistantStateFlow.emit(Resource.Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                _assistantStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }


    fun deleteAssistantUsingId(assistantId: String){
        try {
            _statusLiveData.postValue(Resource.Loading())
            CoroutineScope(Dispatchers.IO).launch {
                async {

                    assistantDao.deleteChatUsingAssistantId(assistantId)
                }.await()

                val result = assistantDao.deleteAssistantUsingId(assistantId)
                handleResult(result,"LÖSCHEN Assistant und Chat geklappt", StatusResult.Deleted)
            }
        }catch (e:Exception){
            _statusLiveData.postValue(Resource.Error(e.message.toString()))
        }

    }
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
    fun updateAssistant(assistant: Assistant) {
        try {
            _statusLiveData.postValue(Resource.Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = assistantDao.updateAssistant(assistant)
                handleResult(result, "Updated Assistant Successfully", StatusResult.Updated)
            }
        } catch (e: Exception) {
            e.printStackTrace()
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