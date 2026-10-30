package com.example.solargrid.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.solargrid.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class GridOperatorDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_grid_operator_dashboard)

        val mainView = findViewById<View>(R.id.main)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navHostFragmentView = findViewById<View>(R.id.grid_operator_nav_host_fragment)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            navHostFragmentView?.setPadding(
                navHostFragmentView.paddingLeft,
                systemBars.top,
                navHostFragmentView.paddingRight,
                0
            )

            bottomNavigation.setPadding(
                bottomNavigation.paddingLeft,
                bottomNavigation.paddingTop,
                bottomNavigation.paddingRight,
                systemBars.bottom
            )

            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.grid_operator_nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        bottomNavigation.setupWithNavController(navController)
    }
}
