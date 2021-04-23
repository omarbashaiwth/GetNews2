package com.omarahmed.getnews2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.omarahmed.getnews2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.exploreFragment,
            R.id.bookmarksFragment
        ))
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()


        binding.apply {
            bottomNavigationView.setupWithNavController(navController)
            toolbar.setupWithNavController(navController,appBarConfiguration)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.bookmarksFragment -> ivSearch.visibility = View.GONE
                    R.id.searchFragment -> {
                        toolbar.visibility = View.GONE
                        bottomNavigationView.visibility = View.GONE
                    }
                    else -> {
                        ivSearch.visibility = View.VISIBLE
                        toolbar.visibility = View.VISIBLE
                        bottomNavigationView.visibility = View.VISIBLE

                    }
                }
            }
            ivSearch.setOnClickListener {
                navHostFragment.findNavController().navigate(R.id.action_global_searchFragment)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}