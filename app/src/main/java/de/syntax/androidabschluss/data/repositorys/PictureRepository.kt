package de.syntax.androidabschluss.data.repositorys

import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.adapter.local.PictureDao
import de.syntax.androidabschluss.data.model.open.PictureItem

class PictureRepository(pictureDao: PictureDao) {
    val allPictures: LiveData<List<PictureItem>> = pictureDao.getAllPictures()
}
