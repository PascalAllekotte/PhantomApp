package de.syntax.androidabschluss.data.repositorys

import NoteDataBase
import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.data.model.open.NoteItem
import de.syntax.androidabschluss.data.model.open.VocabItem


const val TAG = "NoteRepository"
class NoteRepository(private val database: NoteDataBase){

    private val noteDataBaseDao = database.noteDataBaseDao()

    val noteListe: LiveData<List<NoteItem>> = database.noteDataBaseDao().getAllNoteItems()


    suspend fun insert(note: NoteItem){
        try {
            database.noteDataBaseDao().insert(note)
        }catch (e: Exception){
            Log.e(TAG, "Error inserting into DatabaseNote: $e")
        }
    }

    suspend fun update(note: NoteItem) {
        try {
            database.noteDataBaseDao().update(note)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }


}
