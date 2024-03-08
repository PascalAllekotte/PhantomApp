package de.syntax.androidabschluss.adapter.local


import androidx.room.Database
import androidx.room.Room
import android.content.Context  // Corrected import
import android.util.Log
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.open.VocabItem


@Database(entities = [VocabItem::class], version = 1)
abstract class VokabelDataBase : RoomDatabase(){

    abstract fun vokabelDataBaseDao(): VokabelDataBaseDao
}

private lateinit var INSTANCE: VokabelDataBase

fun getDatabase(context: Context): VokabelDataBase {

    synchronized(VokabelDataBase::class.java){
        if(!::INSTANCE.isInitialized){
            Log.d("DatabaseInitialization", "Initialisiere die Vokabel-Datenbank")
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VokabelDataBase::class.java,
                "vokabelc_database"
            ).build()
            Log.d("DatabaseInit", "Datenbank initialisiert")
        }
        Log.d("DatabaseInit", "Datenbankinstanz zurückgegeben")
        return INSTANCE
    }
}
