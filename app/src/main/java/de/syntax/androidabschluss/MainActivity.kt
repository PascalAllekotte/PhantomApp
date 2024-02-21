package de.syntax.androidabschluss

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.random.ui.AddFragment
import de.syntax.androidabschluss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)



        navHost.navController.addOnDestinationChangedListener{_, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> binding.toolbar.visibility = View.GONE
                R.id.homeFragment -> binding.toolbar2.visibility = View.GONE

                R.id.addFragment -> binding.toolbar.visibility = View.VISIBLE
                R.id.addFragment -> binding.toolbar2.visibility = View.VISIBLE



                //---------------------------------------------------
                R.id.homeFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.loginFragment-> binding.bottomNavigationView.visibility = View.GONE
                R.id.registrationFragment-> binding.bottomNavigationView.visibility = View.GONE
                R.id.addFragment-> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }}


    }
