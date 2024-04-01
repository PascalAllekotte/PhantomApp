package de.syntax.androidabschluss

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import de.syntax.androidabschluss.adapter.local.VokabelDataBase
import de.syntax.androidabschluss.databinding.ActivityMainBinding
import de.syntax.androidabschluss.utils.NetworkConnectivityObserver
import de.syntax.androidabschluss.utils.NetworkStatus
import de.syntax.androidabschluss.utils.appSettingOpen
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.utils.warningPermissionDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataBase: VokabelDataBase

    private val auth = FirebaseAuth.getInstance()

    private val networkConnectivityObserver: NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }
    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf()
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)



        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                //---------------------------------------------------
                R.id.homeFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.loginFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.registrationFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.addVokabelFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.noteDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.learningDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.learningFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.pictureGeneratorFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.assistantDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.gptFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.assistantsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.neumorphCardView.visibility = View.GONE
                    binding.view3.visibility = View.GONE
                }

                R.id.deepLFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.picturesFragment -> binding.bottomNavigationView.visibility = View.GONE


                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.neumorphCardView.visibility = View.VISIBLE
                    binding.view3.visibility = View.VISIBLE
                }
            }
        }

        // Überprüfen, ob der Benutzer angemeldet ist, und bei Bedarf zur Hauptseite navigieren
        if (auth.currentUser != null) {
            // Stelle sicher, dass du im Navigation-Graphen eine Aktion oder einen globalen Übergang hast, die/es dich von der Startdestination zum mainFragment bringt
            navController.navigate(R.id.loginFragment)
        }


        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "No Internet Connection",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Wifi") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        networkConnectivityObserver.observe(this) {
            when (it) {
                NetworkStatus.Available -> {
                    if (snackbar.isShown) {
                        snackbar.dismiss()
                    }
                }
                else -> {
                    snackbar.show()
                }
            }
        }
        checkMultiplePermission()
    }
    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    longToastShow("here all permission granted successfully")
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        // here app Setting open because all permission is not granted
                        // and permanent denied
                        appSettingOpen(this)
                    } else {
                        // here warning permission show
                        warningPermissionDialog(this) { _: DialogInterface, which: Int ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE ->
                                    checkMultiplePermission()
                            }
                        }
                    }
                }
            }
        }
    }

}
