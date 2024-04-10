package de.syntax.androidabschluss.data.repositorys

import de.syntax.androidabschluss.adapter.local.UserSettingsDao
import de.syntax.androidabschluss.data.model.open.UserSettings


class UserSettingsRepository private constructor(private val userSettingsDao: UserSettingsDao) {

    suspend fun saveSettings(userSettings: UserSettings) {
        userSettingsDao.insertAllSettings(userSettings)
    }

    suspend fun getSettings(): UserSettings? {
        return userSettingsDao.getSettings()
    }


}
