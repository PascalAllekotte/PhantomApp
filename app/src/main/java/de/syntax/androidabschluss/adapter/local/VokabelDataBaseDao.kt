package de.syntax.androidabschluss.adapter.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.model.open.VocabItem


//tes
@Dao
interface VokabelDataBaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vokabeln: VocabItem)

    @Update
    suspend fun update(vokabeln: VocabItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vokabelnList: List<VocabItem>)

    @Query("SELECT * FROM VocabItem")
    suspend fun getAllVocabItems(): List<VocabItem>



}