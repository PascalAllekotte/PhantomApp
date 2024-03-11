package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.syntax.androidabschluss.adapter.local.getDatabase

class VokabelViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)




}