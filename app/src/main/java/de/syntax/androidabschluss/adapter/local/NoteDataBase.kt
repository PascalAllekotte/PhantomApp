package de.syntax.androidabschluss.adapter.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.open.NoteItem

@Database(entities = [NoteItem::class], version = 1)
abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDataBaseDao(): NoteDataBaseDao
}

private lateinit var INSTANCE: NoteDataBase

fun getDatabaseNote(context: Context): NoteDataBase {

    synchronized(NoteDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            Log.d("DatabaseInitialization", "Initialisiere die Notiz-Datenbank")
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NoteDataBase::class.java,
                "note_database"
            ).build()
            Log.d("DatabaseInit", "Datenbank initialisiert")
        }
        Log.d("DatabaseInit", "Datenbankinstanz zurückgegeben")
        return INSTANCE
    }
}
