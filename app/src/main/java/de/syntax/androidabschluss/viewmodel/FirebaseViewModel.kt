package de.syntax.androidabschluss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    private val _currentUserName = MutableLiveData<String?>()
    val currentUserName: LiveData<String?>
        get() = _currentUserName

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String>
        get() = _loginError

    private val _userEmail = MutableLiveData<String?>(firebaseAuth.currentUser?.email)
    val userEmail: LiveData<String?> get() = _userEmail

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _currentUser.value = firebaseAuth.currentUser
        _userEmail.value = firebaseAuth.currentUser?.email
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }
    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }


    private fun fetchUserName() {
        _currentUser.value?.uid?.let { userId ->
            firestore.collection("users").document(userId).get().addOnSuccessListener { document ->
                val userName = document.getString("username")
                _currentUserName.value = userName
            }.addOnFailureListener {
                _currentUserName.value = null
            }
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = null
        _currentUserName.value = null
    }
}
