package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _strokecolor = MutableLiveData<Int>()
    val strokecolor: LiveData<Int>
        get() = _strokecolor


    fun updatecolor(color : Int){
        _strokecolor.value = color
    }






}