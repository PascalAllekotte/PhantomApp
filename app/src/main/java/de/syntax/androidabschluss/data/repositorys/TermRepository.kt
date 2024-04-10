package de.syntax.androidabschluss.data.repositorys

import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.adapter.local.TermDatabase
import de.syntax.androidabschluss.data.model.open.TermItem


const val TAG3 = "TermRepository"
class TermRepository(private val database: TermDatabase) {

    val termLsite: LiveData<List<TermItem>> = database.termDatabaseDao().getAllTermItems()

    suspend fun insert(term: TermItem){
        try {
            database.termDatabaseDao().insert(term)
        }catch (e: Exception){
            Log.e(TAG3, "Error inserting term into database")
        }
    }

    suspend fun delete(term: TermItem){
        try {
            database.termDatabaseDao().deleteTerm(term)
        }catch (e: Exception){
            Log.e(TAG3, "Error deleting term from database")
        }
    }
    suspend fun insertAll(term: TermItem){
        try {
            database.termDatabaseDao().insertAll(term)
        }catch (e: Exception){
            Log.e(TAG3, "Error inserting allterms into database")
        }
    }

    suspend fun update(term: TermItem){
        try {
            database.termDatabaseDao().update(term)
        }catch (e: Exception){
            Log.e(TAG3, "Error updateing Term database")
        }
    }

    fun getAllTermItems(term: TermItem){
        try {
            database.termDatabaseDao().getAllTermItems()
        }catch (e: Exception){
            Log.e(TAG3, "Error get all term from database")
        }
    }
}