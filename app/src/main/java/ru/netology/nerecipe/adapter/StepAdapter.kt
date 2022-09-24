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

class StepAdapter : ListAdapter<Step, StepAdapter.StepViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StepViewHolderBinding.inflate(inflater, parent, false)
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StepViewHolder(
        private val binding: StepViewHolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var step: Step

        init {
            binding.deleteStepButton.visibility = View.GONE
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
}