package ru.netology.nerecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.databinding.StepViewHolderBinding

class IndexedStepAdapter(
    private val interactionListener: StepInteractionListener
) : ListAdapter<Step, IndexedStepAdapter.StepViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StepViewHolderBinding.inflate(inflater, parent, false)
        return StepViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class StepViewHolder(
        private val binding: StepViewHolderBinding,
        listener: StepInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step

        init {
            binding.deleteStepButton.setOnClickListener { listener.indexedStepDelete(step) }
        }

        fun bind(step: Step, position: Int) {
            this.step = step
            with(binding) {
                stepTitle.text = "Этап №${position + 1}"
                stepText.text = step.text
                //картинка да - показать нет - спрятать
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Step>() {
        override fun areItemsTheSame(oldItem: Step, newItem: Step) =
            oldItem.position == newItem.position

        override fun areContentsTheSame(oldItem: Step, newItem: Step) =
            oldItem == newItem
    }
}