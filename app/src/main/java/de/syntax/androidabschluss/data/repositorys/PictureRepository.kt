package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.data.local.PictureDatabase
import de.syntax.androidabschluss.data.model.open.PictureItem

const val TAG2 = "PictureRepository"
class PictureRepository(private val database: PictureDatabase) {
    val pictureList: LiveData<List<PictureItem>> = database.pictureDataBaseDao().getAllPictures()

    suspend fun delete(picture: PictureItem) {
        try {
            database.pictureDataBaseDao().deletePicture(picture)
        } catch (e: Exception) {
            Log.e(TAG2, "Error updating database: $e")
        }
    }
}
