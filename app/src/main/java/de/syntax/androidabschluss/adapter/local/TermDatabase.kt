package de.syntax.androidabschluss.adapter.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.model.open.TermItem


@Database(entities = [TermItem::class], version = 1)
abstract class TermDatabase : RoomDatabase(){

    abstract fun termDatabaseDao(): TermDatabaseDao
}

private lateinit var INSTANCE: TermDatabase

fun getDatabaseTerm(context: Context): TermDatabase{

    synchronized(TermDatabase::class.java) {
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TermDatabase::class.java,
                "term_database"
            ).build()
        }
        return INSTANCE
    }
}