package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow(repository.getRecipes())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        _recipes.value = repository.searchRecipes(newQuery)
    }

    fun getRecipeById(recipeId: Int) : Recipe? {
        return repository.getRecipeById(recipeId = recipeId)
    }

    fun resetSearch() {
        _query.value = ""
        _recipes.update { repository.getRecipes() }
    }

}