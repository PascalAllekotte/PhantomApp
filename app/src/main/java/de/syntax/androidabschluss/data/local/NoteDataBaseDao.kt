package de.syntax.androidabschluss.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.model.open.NoteItem

@Dao
interface NoteDataBaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteItem)

    @Update
    suspend fun update(note: NoteItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noteList: List<NoteItem>)

    @Delete
    suspend fun deleteNote(note: NoteItem)

    @Query("SELECT * FROM NoteItem")
    fun getAllNoteItems(): LiveData<List<NoteItem>>




}
