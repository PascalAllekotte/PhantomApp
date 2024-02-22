package de.syntax.androidabschluss.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.syntax.androidabschluss.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var auth: FirebaseAuth
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)



        drehung()
        drehung2()





        binding.btnAddUser.setOnClickListener {
            drehen3()
            val friendName = binding.userNameInput.text.toString()
            if (friendName.isNotEmpty()) {
                addFriend(friendName)
            } else {
                Toast.makeText(requireContext(), "Bitte Benutzernamen eingeben.", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun addFriend(friendName: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Nicht angemeldet.", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference.child("users").orderByChild("username").equalTo(friendName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val userUid = snapshot.key
                        if (userUid != null && userUid != userId) {
                            databaseReference.child("users").child(userId).child("friends").child(userUid).setValue(true)
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "Der Benutzer existiert nicht.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "addFriend:onCancelled", databaseError.toException())
            }
        })
    }
    private fun drehung() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 5000
        rotateAnimation.repeatCount = Animation.INFINITE
        binding.schraube.startAnimation(rotateAnimation)
    }

    private fun drehung2() {
        val rotateAnimation = RotateAnimation(
            100f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 4500
        rotateAnimation.repeatCount = Animation.INFINITE
        binding.schraube2.startAnimation(rotateAnimation)
    }
    private fun drehen3() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 2000
        rotateAnimation.repeatCount = Animation.INFINITE
        binding.btnAddUser.startAnimation(rotateAnimation)
    }


}
