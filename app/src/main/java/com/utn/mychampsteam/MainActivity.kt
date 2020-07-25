package com.utn.mychampsteam

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.utn.mychampsteam.fragments.Settings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_settings -> {
//                val ft = supportFragmentManager.beginTransaction().replace(R.id.drawerLayout, Settings())
//                if(!supportFragmentManager.isStateSaved)
//                    ft.commit()
//                else if(true)
//                    ft.commitAllowingStateLoss()
//                replaceFramenty(fragment= Settings(), allowStateLoss=true, containerViewId=R.id.drawer_main_Layout)
//                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                ShowSettings()
            }
            R.id.nav_logout -> {
                Logout()
                ShowAuthentication()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun Logout() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        FirebaseAuth.getInstance().signOut()
    }

    private fun ShowAuthentication() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        this?.startActivity(intent)
        this?.finish()
    }

    private fun ShowSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        this?.startActivity(intent)
        this?.finish()
    }

    private fun ShowAlert(error: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(error)
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


