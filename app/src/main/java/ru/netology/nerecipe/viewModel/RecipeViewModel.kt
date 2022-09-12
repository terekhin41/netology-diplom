package ru.netology.nerecipe.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.nerecipe.Recipe
import ru.netology.nerecipe.Step
import ru.netology.nerecipe.adapter.StepInteractionListener
import ru.netology.nerecipe.impl.AllRecipeDB
import ru.netology.nerecipe.impl.RecipeRepository
import ru.netology.nerecipe.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.impl.StepDB
import ru.netology.nmedia.util.SingleLiveEvent

class RecipeViewModel (application: Application
) : AndroidViewModel(application), StepInteractionListener {


    private val repository: RecipeRepository =
        RecipeRepositoryImpl(
            recipeDao = AllRecipeDB.getInstance(
                context = application
            ).recipeDao,
            stepDao = StepDB.getInstance(
                context = application
            ).stepDao
        )

    val allRecipeData by repository::allRecipeData
    val favoriteRecipeData by repository::favoriteRecipeData

    val favoriteRecipe = SingleLiveEvent<String>()
    val navigateToCreateRecipe = SingleLiveEvent<Long>()
    val navigateToAddImage = SingleLiveEvent<String>()
    val navigateToRecipe = SingleLiveEvent<Long>()
    val indexedSteps = SingleLiveEvent<List<Step>>()
    private var currentRecipe : Recipe? = null

    fun createIndexedStep(position: Int, text: String, imgPath: String? = null) {
        val step = Step(
            id = RecipeRepository.NEW_STEP_ID,
            position = position,
            text = text,
            imgPath = imgPath)
        indexedSteps.value = indexedSteps.value?.plus(listOf(step)) ?: listOf(step)
        println(indexedSteps.value)
    }

    override fun indexedStepDelete(step: Step) {
        indexedSteps.value = indexedSteps.value
            ?.filter { it != step }
            ?.mapIndexed { position, oldStep ->
                oldStep.copy(position = position) }
    }

    fun saveRecipe(title: String, category: String, author: String) {
        val recipe = currentRecipe?.copy(
            title = title,
            category = category,
            author = author,
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            title = title,
            category = category,
            author = author
        )
        recipe.steps = indexedSteps.value ?: emptyList()
        repository.save(recipe)
        currentRecipe = null
        indexedSteps.value = emptyList()
    }

    /*fun createPost(content: String) {
        if (content.isBlank()) return
        val post = currentPost?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Нетология — обучение современным профессиям онлайн",
            content = content,
            published = "Today"
        )
        repository.save(post)
        currentPost = null
    }

    override fun onAddClicked() {
        currentPost = null
        navigateToCreatePost.call()
    }

    override fun onLikeClicked(post: Post) {
        repository.like(post.id)
    }

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }

    override fun onRemoveClicked(post: Post) {
        repository.delete(post.id)
    }

    override fun onEditClicked(post: Post) {
        currentPost = post
        navigateToCreatePost.value = post.content
    }

    override fun onPlayClicked(post: Post) {
        val url : String = if (post.videoUrl != null) post.videoUrl!! else return
        navigateToPlayVideo.value = url
    }

    override fun onPostClicked(post: Post) {
        navigateToPost.value = post.id
    }

    fun getPostById(id: Long): Post? {
        return repository.getPostById(id)
    }*/

}