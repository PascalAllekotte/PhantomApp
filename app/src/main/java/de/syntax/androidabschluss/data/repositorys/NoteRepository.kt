package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.adapter.local.NoteDataBase
import de.syntax.androidabschluss.data.model.open.NoteItem


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
    suspend fun delete(note: NoteItem) {
        try {
            database.noteDataBaseDao().deleteNote(note)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    suspend fun update(note: NoteItem) {
        try {
            database.noteDataBaseDao().update(note)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    suspend fun insertAll(noteItemList: List<NoteItem>) {
        try {
            database.noteDataBaseDao().insertAll(noteItemList)
        } catch (e: Exception) {
            Log.e(V_TAG, "Error inserting list into database: $e")
        }
    }
    suspend fun insertAllNoteItems(noteList: List<NoteItem>){
        try {
            database.noteDataBaseDao().insertAll(noteList)
        }catch (e: Exception){
            Log.e(TAG, "Error get all items: $e")
        }
    }

    suspend fun getAllNoteItems(note: NoteItem){
        try {
            database.noteDataBaseDao().getAllNoteItems()
        }catch (e: Exception){
            Log.e(TAG, "Error get all items: $e")
        }
    }


}
