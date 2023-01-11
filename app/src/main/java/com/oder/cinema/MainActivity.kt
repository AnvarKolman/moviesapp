package com.oder.cinema

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oder.cinema.databinding.ActivityMainBinding
import com.oder.cinema.di.AppComponent
import com.oder.cinema.di.DaggerAppComponent

class MainApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> this.applicationContext.appComponent
    }

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val buttonNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navigation = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.moviesFragment,
                R.id.favoriteMoviesFragment,
                R.id.settingsFragment
            )
        )*/
        if (navigation != null) {
            //setupActionBarWithNavController(navigation, appBarConfiguration)
            buttonNavigationView.setupWithNavController(navigation)
        }
    }
}