package de.syntax.androidabschluss.data.repositorys

import NoteDataBase
import android.content.Context
import de.syntax.androidabschluss.adapter.local.NoteDataBaseDao
import de.syntax.androidabschluss.data.model.open.NoteItem
import getDatabase

object NoteRepository {
    private lateinit var db: NoteDataBase
    private lateinit var noteDao: NoteDataBaseDao

    fun initialize(context: Context) {
        db = getDatabase(context)
        noteDao = db.noteDataBaseDao()
    }

    suspend fun addNote(title: String, content: String) {
        val newNote = NoteItem(title = title, content = content)
        noteDao.insert(newNote)
    }
}
