package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.adapter.dragAndDrop.ItemTouchHelperAdapter
import ru.netology.nerecipe.databinding.RecipeViewHolderBinding
import java.util.*

class RecipeAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DiffCallback),
    ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeViewHolderBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecipeViewHolder(
        private val binding: RecipeViewHolderBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        init {
            binding.root.setOnClickListener { listener.navigateToRecipe(recipe.id) }
            binding.favorite.setOnClickListener { listener.favorite(recipe.id) }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                recipeTitle.text = recipe.title
                favorite.isChecked = recipe.favorite
                category.text = recipe.category
                authorText.text = recipe.author
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                val tempList = currentList.toList()
                Collections.swap(tempList, i, i + 1)
                submitList(tempList)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                val tempList = currentList.toList()
                Collections.swap(tempList, i, i - 1)
                submitList(tempList)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDrop() {}
}


