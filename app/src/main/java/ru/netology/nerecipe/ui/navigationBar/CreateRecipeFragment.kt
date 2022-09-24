package ru.netology.nerecipe.ui.navigationBar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nerecipe.Category
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapter.IndexedStepAdapter
import ru.netology.nerecipe.adapter.dragAndDrop.SimpleItemTouchHelperCallback
import ru.netology.nerecipe.databinding.CreateRecipeFragmentBinding
import ru.netology.nerecipe.util.sendMyNotification
import ru.netology.nerecipe.viewModel.RecipeViewModel

class CreateRecipeFragment : Fragment() {

    private lateinit var categoryArrayAdapter : ArrayAdapter<String>

    private lateinit var binding: CreateRecipeFragmentBinding

    private val viewModel: RecipeViewModel by activityViewModels()

    private var stepIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryList = Category.values().map { it.key }
        categoryArrayAdapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            categoryList
        )
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CreateRecipeFragmentBinding.inflate(inflater, container, false).also { binding ->
        this.binding = binding
        binding.categorySpinner.adapter = categoryArrayAdapter
        val adapter = IndexedStepAdapter(viewModel)
        binding.stepsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.stepsRecyclerView.adapter = adapter
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.stepsRecyclerView)
        binding.createStepView.stepTitle.text = "Этап №1"

        viewModel.indexedSteps.observe(viewLifecycleOwner) { steps ->
            adapter.submitList(steps)
            stepIndex = steps.size
            prepareStepView()
        }

        with(binding.createStepView) {
            val image = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
                if (it != null) {
                    stepImage.setImageURI(it)
                    stepImage.tag = it
                    stepImage.isClickable = false
                    deleteStepImageButton.visibility = View.VISIBLE
                }
            }
            stepImage.setOnClickListener {
                image.launch(arrayOf("image/*"))
            }
            deleteStepImageButton.setOnClickListener {
                stepImage.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
                stepImage.tag = null
                deleteStepImageButton.visibility = View.GONE
                stepImage.isClickable = true
            }
            deleteStepButton.setOnClickListener {
                root.visibility = View.GONE
                prepareStepView()
            }
        }

        binding.addStepButton.setOnClickListener {
            if (binding.createStepView.root.visibility == View.GONE) {
                binding.createStepView.root.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                val text = binding.createStepView.stepText.text
                val imgUri = binding.createStepView.stepImage.tag
                if (text.isNullOrBlank() && imgUri == null) {
                    sendMyNotification(this.requireContext(), "Этап не может быть пустым")
                    return@setOnClickListener
                } else {
                    viewModel.createIndexedStep(
                        position = stepIndex,
                        text = text.toString(),
                        imgPath = imgUri?.toString()
                    )
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEdit.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val author = binding.authorEdit.text.toString()
            if (title.isBlank() || author.isBlank()) {
                sendMyNotification(
                    this.requireContext(),
                    "Все поля должны быть заполнены"
                )
                binding.scrollView.scrollTo(0, 0)
                return@setOnClickListener
            }
            binding.titleEdit.text = null
            binding.authorEdit.text = null
            sendMyNotification(this.requireContext(), "Рецепт успешно сохранён")
            if (viewModel.currentRecipe != null) {
                viewModel.saveRecipe(title, category, author)
                activity?.supportFragmentManager?.popBackStack()
            } else viewModel.saveRecipe(title, category, author)
        }

        val recipeForEdit = viewModel.currentRecipe
        if (recipeForEdit != null) {
            with(binding) {
                titleEdit.setText(recipeForEdit.title)
                authorEdit.setText(recipeForEdit.author)
                viewModel.indexedSteps.value = viewModel.getRecipesStepsById(recipeForEdit.id)
            }
        }
    }.root

    private fun prepareStepView() {
        with(binding.createStepView) {
            stepTitle.text = "Этап №${stepIndex + 1}"
            stepText.setText("")
            stepImage.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
            stepImage.tag = null
            stepImage.isClickable = true
            deleteStepImageButton.visibility = View.GONE
        }
    }
}