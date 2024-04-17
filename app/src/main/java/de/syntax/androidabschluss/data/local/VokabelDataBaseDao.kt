package de.syntax.androidabschluss.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.model.open.VocabItem



@Dao
interface VokabelDataBaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vokabeln: VocabItem)

    @Update
    suspend fun update(vokabeln: VocabItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vokabelnList: List<VocabItem>)

    @Query("SELECT * FROM VocabItem")
    fun getAllVocabItems(): LiveData<List<VocabItem>>

    @Query("SELECT DISTINCT block FROM VocabItem")
    fun getUniqueBlocks(): LiveData<List<String>>

    @Query("SELECT * FROM VocabItem WHERE block = :blockName")
    fun getVocabItemsByBlock(blockName: String): LiveData<List<VocabItem>>

}