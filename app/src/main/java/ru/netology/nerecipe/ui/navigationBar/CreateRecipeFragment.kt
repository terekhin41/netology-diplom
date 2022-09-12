package ru.netology.nerecipe.ui.navigationBar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nerecipe.Category
import ru.netology.nerecipe.adapter.IndexedStepAdapter
import ru.netology.nerecipe.databinding.CreateRecipeFragmentBinding
import ru.netology.nerecipe.util.sendMyNotification
import ru.netology.nerecipe.viewModel.RecipeViewModel

class CreateRecipeFragment : Fragment() {

    private lateinit var categoryArrayAdapter : ArrayAdapter<String>

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
        binding.categorySpinner.adapter = categoryArrayAdapter
        val adapter = IndexedStepAdapter(viewModel)
        binding.stepsRecyclerView.layoutManager = LinearLayoutManager(context);
        binding.stepsRecyclerView.adapter = adapter
        binding.createStepView.stepTitle.text = "Этап №1"

        viewModel.indexedSteps.observe(viewLifecycleOwner) { steps ->
            adapter.submitList(steps)
            stepIndex = steps.size + 1
            binding.createStepView.stepTitle.text = "Этап №${stepIndex}"
  1      }

        with(binding.createStepView) {
            deleteStepButton.setOnClickListener {
                it.visibility = View.GONE
            }
            stepImage.setOnClickListener {

                TODO("Добавление картинки из галерии") //сюда clickable = false
                    // в результат запихнуть крестик
            }
            deleteStepImageButton.setOnClickListener {
                TODO("Удаление изображения") //clickable = true
            }
            deleteStepButton.setOnClickListener {
                //TODO Удали картинку
                stepText.setText("")
                root.visibility = View.GONE
            }
        }

        binding.addStepButton.setOnClickListener {
            if (binding.createStepView.root.visibility == View.GONE) {
                binding.createStepView.root.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                if (binding.createStepView.stepText.text.isBlank()) {   //TODO && нет картинки
                    sendMyNotification(this.requireContext(), "Текст не может быть пустым")
                    return@setOnClickListener
                } else {
                    stepIndex++
                    viewModel.createIndexedStep(
                        stepIndex,
                        binding.createStepView.stepText.text.toString()
                    ) //TODO тут путь на картинку
                    binding.createStepView.stepTitle.text = "Этап №${stepIndex}"
                    // TODO снести картинку
                    binding.createStepView.stepText.setText("")
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
                    "Все поля должны быть заполнены"  // TODO hardcode
                )
                binding.scrollView.scrollTo(0, 0)
                return@setOnClickListener
            }
            viewModel.saveRecipe(title, category, author)
        }
    }.root
}