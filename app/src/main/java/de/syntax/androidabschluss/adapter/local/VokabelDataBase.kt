package de.syntax.androidabschluss.adapter.local

import androidx.room.Database
import androidx.room.Room
import android.content.Context  // Corrected import

import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.open.VocabItem

@Database(entities = [VocabItem::class], version = 1)
abstract class VokabelDataBase : RoomDatabase(){

    abstract val dao: VokabelDataBase
}

private lateinit var INSTANCE: VokabelDataBase

fun getDatabase(context: Context): VokabelDataBase {

    synchronized(VokabelDataBase::class.java){
        if(!::INSTANCE.isInitialized){


            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                VokabelDataBase::class.java,
                "vokabel_database"
            ).build()
        }
        return INSTANCE
    }
}
