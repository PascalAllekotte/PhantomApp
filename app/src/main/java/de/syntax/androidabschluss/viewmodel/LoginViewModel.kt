package de.syntax.androidabschluss.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userLiveData = MutableLiveData<FirebaseUser?>()
    val userLiveData: LiveData<FirebaseUser?> = _userLiveData

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.ERROR("E-Mail und Passwort dürfen nicht leer sein.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginState.value = LoginState.SUCCESS
                    _userLiveData.value = auth.currentUser
                } else {
                    _loginState.value =
                        LoginState.ERROR(task.exception?.message ?: "Anmeldung fehlgeschlagen")
                }
            }
    }



    sealed class LoginState {
        object SUCCESS : LoginState()
        data class ERROR(val message: String) : LoginState()
    }
}
