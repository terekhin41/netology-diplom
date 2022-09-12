package ru.netology.nerecipe.ui.navigationBar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nerecipe.databinding.FavoriteRecipeListBinding

class FavoriteRecipeListFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FavoriteRecipeListBinding.inflate(inflater, container, false).root
}