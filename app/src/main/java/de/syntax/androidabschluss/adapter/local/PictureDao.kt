package de.syntax.androidabschluss.adapter.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.syntax.androidabschluss.data.model.open.PictureItem


    @Dao
    interface PictureDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPicture(pictureItem: PictureItem)


        @Query("SELECT * FROM pictures3")
        fun getAllPictures(): LiveData<List<PictureItem>>


    }