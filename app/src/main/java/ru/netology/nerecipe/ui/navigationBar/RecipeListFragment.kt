package ru.netology.nerecipe.ui.navigationBar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import ru.netology.nerecipe.Category
import ru.netology.nerecipe.adapter.CategoriesAdapter
import ru.netology.nerecipe.adapter.RecipeAdapter
import ru.netology.nerecipe.adapter.dragAndDrop.SimpleItemTouchHelperCallback
import ru.netology.nerecipe.databinding.RecipeListBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class RecipeListFragment : Fragment() {

    private val viewModel: RecipeViewModel by activityViewModels()

    private lateinit var categoriesAdapter : CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeListBinding.inflate(inflater, container, false)
        .also { binding ->
            val recipeAdapter = RecipeAdapter(viewModel)
            binding.recipeRecycleView.adapter = recipeAdapter
            val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(recipeAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.recipeRecycleView)
            binding.recipeRecycleView.requestFocus()

            categoriesAdapter = CategoriesAdapter(viewModel)
            binding.filters.checkboxList.adapter = categoriesAdapter

            categoriesAdapter.submitList(Category.values().toList())
            viewModel.filterAllRecipes()

            viewModel.allRecipeData.observe(viewLifecycleOwner) { recipes ->
                recipeAdapter.submitList(recipes)
                viewModel.addDefaultData()
            }

            viewModel.filteredAllRecipesList.observe(viewLifecycleOwner) { filteredRecipes ->
                recipeAdapter.submitList(filteredRecipes)
            }

            viewModel.recipesFilters.observe(viewLifecycleOwner) {
                viewModel.filterAllRecipes()
            }

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchAllRecipes(newText)
                    return false
                }
            })

            binding.filtersButton.setOnClickListener {
                val visibility = binding.filters.filters.visibility
                binding.filters.filters.visibility =
                    if (visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
        .root
}