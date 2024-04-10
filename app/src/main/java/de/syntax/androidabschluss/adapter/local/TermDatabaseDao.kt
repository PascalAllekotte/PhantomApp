package de.syntax.androidabschluss.adapter.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.model.open.TermItem


@Dao
interface TermDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(term: TermItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(term: TermItem)

    @Update
    suspend fun update(term: TermItem)

    @Delete
    suspend fun deleteTerm(term: TermItem)

    @Query("SELECT * FROM TermItem ORDER BY termDate ASC")
    fun getAllTermItems(): LiveData<List<TermItem>>


}