package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentNavItemId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            if (it.itemId != currentNavItemId) {
                if (it.itemId == R.id.create_recipe_nav_fragment) {
                    binding.searchView.visibility = View.GONE
                    binding.filtersButton.visibility = View.GONE
                } else {
                    binding.searchView.visibility = View.VISIBLE
                    binding.filtersButton.visibility = View.VISIBLE
                }
                currentNavItemId = it.itemId
                navController.navigate(it.itemId)
                true
            } else false
        }
    }
}