package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.data.local.getDatabasePicture
import de.syntax.androidabschluss.data.model.open.PictureItem
import de.syntax.androidabschluss.data.repositorys.PictureRepository
import kotlinx.coroutines.launch

class PicturesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabasePicture(application)
    private val repository = PictureRepository(database)

    val allPictures = repository.pictureList

    private val _pictureComplete = MutableLiveData<Boolean>()
    val pictureComplete: LiveData<Boolean>
        get() = _pictureComplete

 /**   init {
        val pictureDao = getDatabasePicture(application).pictureDataBaseDao()
        repository = PictureRepository(database = pictureDao)
        allPictures = repository.pictureList
    }
**/


 fun delete(picture: PictureItem) {
     viewModelScope.launch {
         repository.delete(picture)
     }
 }
}
