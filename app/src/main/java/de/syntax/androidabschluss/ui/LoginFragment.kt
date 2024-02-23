import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentLoginBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel

class LoginFragment : Fragment() {

    private val viewModel: FirebaseViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }

        binding.btnLoginlog.setOnClickListener {
            val email = binding.logmail.text.toString().trim()
            val password = binding.logpassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Bitte erst Eingabe machen!!!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Benutzer ist angemeldet
            Toast.makeText(context, "Anmeldung erfolgreich!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment) // Passen Sie die Navigation an Ihre Bedürfnisse an
        } else {
            // Benutzer ist nicht angemeldet oder Anmeldung fehlgeschlagen
            Toast.makeText(context, "Anmeldung fehlgeschlagen.", Toast.LENGTH_SHORT).show()
        }
    }
}
