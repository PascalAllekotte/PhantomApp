package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.adapter.local.getDatabasePicture
import de.syntax.androidabschluss.data.repositorys.PictureRepository

class PicturesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabasePicture(application)
    private val repository = PictureRepository(database)

    val allPictures = repository.pictureList

 /**   init {
        val pictureDao = getDatabasePicture(application).pictureDataBaseDao()
        repository = PictureRepository(database = pictureDao)
        allPictures = repository.pictureList
    }
**/



}
