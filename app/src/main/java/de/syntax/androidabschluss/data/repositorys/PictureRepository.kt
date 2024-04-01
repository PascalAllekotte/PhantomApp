package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.adapter.local.PictureDatabase
import de.syntax.androidabschluss.data.model.open.PictureItem

class PictureRepository(database: PictureDatabase) {
    val pictureList: LiveData<List<PictureItem>> = database.pictureDataBaseDao().getAllPictures()

    suspend fun delete(picture: PictureItem) {
        try {
            delete(picture)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }
}
