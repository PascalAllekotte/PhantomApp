package de.syntax.androidabschluss.adapter.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.open.PictureItem

@Database(entities = [PictureItem::class], version = 1)
abstract class PictureDatabase : RoomDatabase() {

    abstract fun pictureDataBaseDao(): PictureDao
}

private lateinit var INSTANCE: PictureDatabase

fun getDatabasePicture(context: Context): PictureDatabase {

    synchronized(PictureDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            Log.d("DatabaseInitialization", "Initialisiere die Notiz-Datenbank")
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PictureDatabase::class.java,
                "picture4_database"
            ).build()
            Log.d("DatabaseInit", "Datenbank initialisiert")
        }
        Log.d("DatabaseInit", "Datenbankinstanz zurückgegeben")
        return INSTANCE
    }
}
