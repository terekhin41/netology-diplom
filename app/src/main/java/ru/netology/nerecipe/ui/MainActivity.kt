package ru.netology.nerecipe.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.databinding.ActivityMainBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel


class MainActivity :
    AppCompatActivity(),
    ExitFromCreateRecipeDialog.NoticeDialogListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: RecipeViewModel by viewModels()
    private var backPressed: Long = 0
    private lateinit var navController : NavController
    private lateinit var navView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            if (it.itemId != navView.selectedItemId) {
                if (navView.selectedItemId == R.id.create_recipe_nav_fragment) {
                    val dialog = ExitFromCreateRecipeDialog()
                    dialog.arguments = Bundle().apply { this.putInt(null, it.itemId) }
                    dialog.show(supportFragmentManager, "CreateRecipeExit")
                    return@setOnItemSelectedListener false
                }
                navController.navigate(it.itemId)
                true
            } else false
        }

        viewModel.navigateToRecipe.observe(this) {
            val intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", it)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (backPressed + DEFAULT_DOUBLE_TAP_DELAY > System.currentTimeMillis()) {
            super.finish()
        } else {
            Toast.makeText(baseContext, "Нажмите еще раз для выхода!", Toast.LENGTH_SHORT).show()
        }
        backPressed = System.currentTimeMillis()
    }

    companion object {
        private const val DEFAULT_DOUBLE_TAP_DELAY = 2_000
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {}

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        val id = dialog.arguments?.getInt(null) ?: R.id.all_recipes_nav_fragment
        navController.navigate(id)
    }
}