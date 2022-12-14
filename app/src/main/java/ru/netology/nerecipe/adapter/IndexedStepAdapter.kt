package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.adapter.dragAndDrop.ItemTouchHelperAdapter
import ru.netology.nerecipe.databinding.StepViewHolderBinding
import java.util.*

class IndexedStepAdapter(
    private val interactionListener: StepInteractionListener
) : ListAdapter<Step, IndexedStepAdapter.StepViewHolder>(DiffCallback),
    ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StepViewHolderBinding.inflate(inflater, parent, false)
        return StepViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StepViewHolder(
        private val binding: StepViewHolderBinding,
        listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step

        init {
            binding.deleteStepButton.setOnClickListener { listener.indexedStepDelete(step) }
        }

        fun bind(step: Step) {
            this.step = step
            with(binding) {
                stepTitle.text = "Этап №${bindingAdapterPosition + 1}"
                stepText.text = step.text
                if (!step.imgPath.isNullOrBlank()) {
                    stepImage.setImageURI(step.imgPath.toUri())
                } else stepImage.visibility = View.GONE
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem.position == newItem.position

        override fun areContentsTheSame(oldItem: Step, newItem: Step) =
            oldItem == newItem
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                val tempList = currentList.toMutableList()
                Collections.swap(tempList, i, i + 1)
                submitList(tempList)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                val tempList = currentList.toMutableList()
                Collections.swap(tempList, i, i - 1)
                submitList(tempList)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDrop() {
        val correctStepsTitlesList = currentList.mapIndexed { index, step ->
            step.copy(position = index)
        }
        submitList(correctStepsTitlesList)
    }
}