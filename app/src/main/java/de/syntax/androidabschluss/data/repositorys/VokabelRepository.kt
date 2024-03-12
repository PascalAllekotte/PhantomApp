package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.adapter.local.VokabelDataBase
import de.syntax.androidabschluss.data.model.open.VocabItem

const val V_TAG = "VokabelRepository"
class VokabelRepository(private val database: VokabelDataBase) {

    private val vokabelDataBaseDao = database.vokabelDataBaseDao()

    val vokabelListe: LiveData<List<VocabItem>> = database.vokabelDataBaseDao().getAllVocabItems()

    val uniqueBlockList: LiveData<List<String>> = vokabelDataBaseDao.getUniqueBlocks()




    suspend fun insert(vokabeln: VocabItem) {
        try {
            database.vokabelDataBaseDao().insert(vokabeln)
        } catch (e: Exception) {
            Log.e(V_TAG, "Error inserting into database: $e")
        }
    }

    suspend fun update(vokabeln: VocabItem) {
        try {
            database.vokabelDataBaseDao().update(vokabeln)
        } catch (e: Exception) {
            Log.e(V_TAG, "Error updating database: $e")
        }
    }

    suspend fun insertAll(vokabelnList: List<VocabItem>) {
        try {
            database.vokabelDataBaseDao().insertAll(vokabelnList)
        } catch (e: Exception) {
            Log.e(V_TAG, "Error inserting list into database: $e")
        }
    }
}
