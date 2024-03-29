package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.adapter.local.getDatabasePicture
import de.syntax.androidabschluss.data.model.open.PictureItem
import de.syntax.androidabschluss.data.repositorys.PictureRepository

class PicturesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PictureRepository
    val allPictures: LiveData<List<PictureItem>>

    init {
        val pictureDao = getDatabasePicture(application).pictureDataBaseDao()
        repository = PictureRepository(pictureDao)
        allPictures = repository.allPictures
    }
}
