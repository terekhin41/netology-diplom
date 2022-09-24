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
import ru.netology.nerecipe.databinding.FavoriteRecipeListBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel


class FavoriteRecipeListFragment : Fragment(){

    private val viewModel: RecipeViewModel by activityViewModels()

    private lateinit var categoriesAdapter : CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoriteRecipeListBinding.inflate(inflater, container, false)
        .also { binding ->
            val adapter = RecipeAdapter(viewModel)
            binding.favoriteRecipeRecycleView.adapter = adapter
            val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.favoriteRecipeRecycleView)
            binding.favoriteRecipeRecycleView.requestFocus()

            categoriesAdapter = CategoriesAdapter(viewModel)
            binding.filters.checkboxList.adapter = categoriesAdapter

            categoriesAdapter.submitList(Category.values().toList())
            viewModel.filterFavoriteRecipes()

            viewModel.favoriteRecipeData.observe(viewLifecycleOwner) { posts ->
                adapter.submitList(posts)
            }

            viewModel.filteredFavoriteRecipesList.observe(viewLifecycleOwner) { filteredRecipes ->
                adapter.submitList(filteredRecipes)
            }

            viewModel.recipesFilters.observe(viewLifecycleOwner) {
                viewModel.filterFavoriteRecipes()
            }

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchFavoriteRecipes(newText)
                    return false
                }
            })

            binding.filtersButton.setOnClickListener {
                val visibility = binding.filters.filters.visibility
                binding.filters.filters.visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
        .root
}