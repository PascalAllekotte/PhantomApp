package de.syntax.androidabschluss.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

const val TAG = "FirebaseViewModel"

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser



    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = firebaseAuth.currentUser
            }else {
                Log.e(TAG, "Login failed: ${it.exception}")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                _currentUser.value = user
                user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                saveUsernameToFirestore(user?.uid, name)
            } else {
                Log.e(TAG, "Registration failed: ${it.exception}")
            }
        }
    }

    private fun saveUsernameToFirestore(userId: String?, name: String) {
        if (userId != null) {
            db.collection("users").document(userId).set(mapOf("username" to name))
                .addOnSuccessListener { Log.d(TAG, "Username saved to Firestore") }
                .addOnFailureListener { e -> Log.w(TAG, "Error saving username", e) }
        }
    }


    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = firebaseAuth.currentUser
    }

}
