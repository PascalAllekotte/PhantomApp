package de.syntax.androidabschluss.ui
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import de.syntax.androidabschluss.databinding.FragmentMainBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var user: FirebaseUser? = null
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register a FragmentResultListener
        parentFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            if (requestKey == "requestKey") {
                user = bundle.getParcelable("user")
                updateUI()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()



        binding.encrypt.setOnClickListener(){
            updateWhenClicked()
        }




    }

    private fun updateUI() {
        user?.let {
            binding.username.text = it.displayName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setUser(user: FirebaseUser) {
        this.user = user
        updateUI()
    }
    fun updateWhenClicked() {
        binding.keyword.visibility = if (binding.encrypt.isChecked) View.VISIBLE else View.GONE



    }
}
