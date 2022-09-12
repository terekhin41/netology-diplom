package ru.netology.nerecipe.ui.navigationBar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.databinding.RecipeListBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel


class RecipeListFragment : Fragment() {

    private val viewModel: RecipeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipe.observe(this) { recipeId ->
            val direction = RecipeListFragmentDirections.allRecipeToRecipeFragment(recipeId)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeListBinding.inflate(inflater, container, false).root
}