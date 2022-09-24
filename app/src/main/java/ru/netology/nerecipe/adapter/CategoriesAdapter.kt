package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.Category
import ru.netology.nerecipe.databinding.CategoryViewHolderBinding
import ru.netology.nerecipe.viewModel.RecipeViewModel

class CategoriesAdapter(
    private val viewModel: RecipeViewModel
    ) : ListAdapter<Category, CategoriesAdapter.CategoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryViewHolderBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(
        private val binding: CategoryViewHolderBinding,
        private val viewModel: RecipeViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var category: Category

        init {
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                val filters = viewModel.recipesFilters.value?.also { it[category] = isChecked }
                viewModel.recipesFilters.value = filters
            }
        }

        fun bind(category: Category) {
            this.category = category
            binding.checkbox.text = category.key
            binding.checkbox.isChecked =
                viewModel.recipesFilters.value?.get(category) ?: true
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}


