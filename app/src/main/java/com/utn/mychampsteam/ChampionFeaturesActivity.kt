package com.utn.mychampsteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChampionFeaturesActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var featuresNavHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_champion_features)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        featuresNavHost = supportFragmentManager.findFragmentById(R.id.features_navhost) as NavHostFragment
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar)

        NavigationUI.setupWithNavController(bottomNavigationBar, featuresNavHost.navController)
    }

        override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        this?.startActivity(intent)
        this?.finish()
    }

    public fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}

