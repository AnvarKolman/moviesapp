package com.oder.cinema.ui

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oder.cinema.R
import com.oder.cinema.di.AppComponent
import com.oder.cinema.di.DaggerAppComponent

class MainApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> this.applicationContext.appComponent
    }

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    //TODO NEED ADD SPLASH SCREEN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)
    }
}