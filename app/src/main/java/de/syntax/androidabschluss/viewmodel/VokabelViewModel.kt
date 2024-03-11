package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.adapter.local.getDatabase
import de.syntax.androidabschluss.data.repositorys.VokabelRepository

class VokabelViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    val repository = VokabelRepository(database)






}