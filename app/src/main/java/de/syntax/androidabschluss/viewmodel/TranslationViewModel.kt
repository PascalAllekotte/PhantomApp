package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import de.syntax.androidabschluss.adapter.Request.DeeplRequest
import de.syntax.androidabschluss.data.repositorys.DeepLRepository
import de.syntax.androidabschluss.utils.Resources
import kotlinx.coroutines.Dispatchers

class TranslationViewModel(private val repository: DeepLRepository) : ViewModel() {
    fun translateText(deeplRequest: DeeplRequest) = liveData(Dispatchers.IO) {
        emit(Resources.loading(data = null))
        try {
            emit(Resources.success(data = repository.translateText(deeplRequest)))
        } catch (exception: Exception) {
            emit(Resources.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}