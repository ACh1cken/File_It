package com.example.fileit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import com.example.fileit.auth.SignupActivity
import com.example.fileit.fragments.AnnouncementFragment
import com.example.fileit.webcrawler.webcrawler
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_main_page.*


//,NavigationView.OnNavigationItemSelectedListener
class MainPage : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        auth = FirebaseAuth.getInstance()
        authStateListener = AuthStateListener {
            val user = auth.currentUser
            if (user == null) {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationview)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navcontroller = navHostFragment.navController
        navigationView.setupWithNavController(navcontroller)
       // navcontroller = findNavController(R.id.fragmentContainerView2)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.announcementFragment,R.id.viewExistingFragment,R.id.createNewEntry,R.id.settingsFragment),drawerLayout)

        setupActionBarWithNavController(navcontroller,drawerLayout)


        navigationView.setNavigationItemSelectedListener{
            Log.e("Listener","Listener called")
            when(it.itemId) {
                R.id.announcementFragment -> {
                    fragmentContainerView2.findNavController().navigate(R.id.announcementFragment,
                        null,
                        navOptions {
                            anim {
                                enter = android.R.animator.fade_in
                                exit = android.R.animator.fade_out
                            }
                        })
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.viewExistingFragment -> {
                    fragmentContainerView2.findNavController().navigate(R.id.viewExistingFragment,
                        null,
                        navOptions {
                            anim {
                                enter = android.R.animator.fade_in
                                exit = android.R.animator.fade_out
                            }
                        })
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.createNewEntry -> {
                    fragmentContainerView2.findNavController().navigate(R.id.createNewEntry,
                        null,
                        navOptions {
                            anim {
                                enter = android.R.animator.fade_in
                                exit = android.R.animator.fade_out
                            }
                        })
//                    fragmentContainerView2.findNavController().navigate(R.id.appStorage)
                    drawerLayout.closeDrawer (GravityCompat.START)
                }
                R.id.settingsFragment -> {
                    fragmentContainerView2.findNavController().navigate(R.id.settingsFragment,
                        null,
                        navOptions {
                            anim {
                                enter = android.R.animator.fade_in
                                exit = android.R.animator.fade_out
                            }
                        })
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }


//    override fun setNavigationViewListener(): Boolean {
//        val navView: NavigationView = findViewById(R.id.navigationview)
//        navView.setNavigationItemSelectedListener(this)
//        return true
//    }
//
//    override fun onNavigationItemSelectedListener(item: MenuItem): Boolean {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
//        val navcontroller = navHostFragment.navController
//        when(item.itemId) {
//            R.id.announcementFragment -> navcontroller.navigate(R.id.announcementFragment)
//            R.id.viewExistingFragment -> navcontroller.navigate(R.id.viewExistingFragment)
//            R.id.createNewEntry -> navcontroller.navigate(R.id.createNewEntry)
//            R.id.settingsFragment -> navcontroller.navigate(R.id.settingsFragment)
//        }
//        return item.onNavDestinationSelected(navcontroller)||return super.onOptionsItemSelected(item)
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navcontroller : NavController = fragmentContainerView2.findNavController()
        Log.e("OnOptionsItemSelected","Called")
        println(item)
        val announcementFragment = AnnouncementFragment()
        when(item.itemId){
            R.id.announcementFragment -> supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,announcementFragment)
        }

        return NavigationUI.onNavDestinationSelected(item,navcontroller)  ||  return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navcontroller = findNavController(R.id.fragmentContainerView2)
        return navcontroller.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}