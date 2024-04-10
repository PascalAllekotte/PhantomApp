package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.adapter.local.UserSettingsDao
import de.syntax.androidabschluss.adapter.local.UserSettingsDatabase
import de.syntax.androidabschluss.data.model.open.UserSettings
import kotlinx.coroutines.launch

class UserSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val userSettingsDao: UserSettingsDao = UserSettingsDatabase.getDatabase(application).userSettingsDao()
    private val _userSettings = MutableLiveData<UserSettings?>()
    val userSettings: LiveData<UserSettings?> get() = _userSettings

    init {
        loadUserSettings()
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            // Angenommen, wir wollen nur eine einzelne UserSettings-Instanz erhalten.
            // Die Implementierung hängt davon ab, wie die Daten in der Datenbank gespeichert sind.
            _userSettings.value = userSettingsDao.getSettings()
        }
    }

    fun saveUserSettings(userSettings: UserSettings) {
        viewModelScope.launch {
            userSettingsDao.insertAllSettings(userSettings)
        }
    }
}

