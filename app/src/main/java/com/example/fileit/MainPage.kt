package com.example.fileit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainPage : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navcontroller : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        toolbar = findViewById(R.id.app_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationview)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navcontroller = navHostFragment.navController
       // navcontroller = findNavController(R.id.fragmentContainerView2)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.announcementFragment,R.id.viewExistingFragment,R.id.createNewEntry,R.id.settingsFragment),drawerLayout)

        setupActionBarWithNavController(navcontroller,drawerLayout)
        navigationView.setupWithNavController(navcontroller)

    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = navcontroller
//        return super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navcontroller = findNavController(R.id.fragmentContainerView2)
        return navcontroller.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}