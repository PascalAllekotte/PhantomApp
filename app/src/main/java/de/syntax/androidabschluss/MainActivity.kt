package de.syntax.androidabschluss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import de.syntax.androidabschluss.adapter.local.VokabelDataBase
import de.syntax.androidabschluss.adapter.local.VokabelDataBaseDao
import de.syntax.androidabschluss.adapter.local.getDatabase
import de.syntax.androidabschluss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataBase: VokabelDataBase

    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = getDatabase(this)




        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)



        navHost.navController.addOnDestinationChangedListener{_, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                //---------------------------------------------------
                R.id.homeFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.loginFragment-> binding.bottomNavigationView.visibility = View.GONE
                R.id.registrationFragment-> binding.bottomNavigationView.visibility = View.GONE
                R.id.addFragment-> binding.bottomNavigationView.visibility = View.GONE
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        // Überprüfen, ob der Benutzer angemeldet ist, und bei Bedarf zur Hauptseite navigieren
        if (auth.currentUser != null) {
            // Stelle sicher, dass du im Navigation-Graphen eine Aktion oder einen globalen Übergang hast, die/es dich von der Startdestination zum mainFragment bringt
            navController.navigate(R.id.loginFragment)
        }
    }
}
