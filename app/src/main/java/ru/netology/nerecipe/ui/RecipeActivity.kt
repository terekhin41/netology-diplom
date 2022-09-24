package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.StepAdapter
import ru.netology.nerecipe.databinding.RecipeActivityBinding
import ru.netology.nerecipe.ui.navigationBar.CreateRecipeFragment
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RecipeActivity : AppCompatActivity(),
    ExitFromCreateRecipeDialog.NoticeDialogListener {

    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var binding: RecipeActivityBinding
    private lateinit var stepAdapter: StepAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecipeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeId = intent.getLongExtra("RECIPE_ID", 0L)
        stepAdapter = StepAdapter()
        binding.stepsList.adapter = stepAdapter

        viewModel.allRecipeData.observe(this) {
            returnToActivity()
            bind(recipeId)
        }

        binding.recipeHeader.favorite.setOnClickListener {
            viewModel.favorite(recipeId)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val popupMenu = PopupMenu(this, binding.options).apply {
            inflate(R.menu.recipe_options)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit -> {
                        viewModel.currentRecipe = viewModel.getRecipeById(recipeId)
                        navigateToEditFragment()
                        true
                    }
                    R.id.delete -> {
                        viewModel.onDeleteClicked(recipeId)
                        this@RecipeActivity.finish()
                        true
                    }
                    else -> false
                }
            }
        }

        binding.options.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun bind(recipeId: Long) {
        //if (!this::stepAdapter.isInitialized)
        if (recipeId == 0L) this.finish()
        val recipe = viewModel.getRecipeById(recipeId)
        val steps = viewModel.getRecipesStepsById(recipeId)
        if (recipe == null || steps == null) this.finish()
        with(binding) {
            recipeHeader.recipeTitle.text = recipe?.title
            recipeHeader.category.text = recipe?.category
            recipeHeader.authorText.text = recipe?.author
            recipeHeader.favorite.isChecked = recipe?.favorite ?: false
        }
        stepAdapter.submitList(steps)
    }

    private fun navigateToEditFragment() {
        binding.title.text = "Редактирование рецепта"
        binding.options.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, CreateRecipeFragment())
            .addToBackStack("DIALOG")
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            ExitFromCreateRecipeDialog().show(supportFragmentManager, "EditRecipeExit")
        else super.onBackPressed()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {}

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        val id = supportFragmentManager.getBackStackEntryAt(0).id
        supportFragmentManager.popBackStack(id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        returnToActivity()
    }

    private fun returnToActivity() {
        viewModel.currentRecipe = null
        binding.title.text = "Просмотр рецепта"
        binding.options.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }
}