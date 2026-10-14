package com.example.pantryai.viewModel

import androidx.lifecycle.ViewModel
import com.example.pantryai.data.model.Recipe
import com.example.pantryai.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow(repository.getRecipes())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow<String?>(null)
    val selectedDifficulty: StateFlow<String?> = _selectedDifficulty.asStateFlow()

    private val _maxCookingTimeMinutes = MutableStateFlow<Int?>(null)
    val maxCookingTimeMinutes: StateFlow<Int?> = _maxCookingTimeMinutes.asStateFlow()

    val allRecipes: List<Recipe> = repository.getRecipes()

    val categories: List<String> = allRecipes.map { it.category }.distinct()

    val difficulties: List<String> = allRecipes.map { it.difficulty }.distinct()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        _recipes.value = repository.searchRecipes(newQuery)
    }

    fun onCategoryChange(category: String?) {
        _selectedCategory.value = category
        applyFilter()
    }
    fun onDifficultyChange(difficulty: String?) {
        _selectedDifficulty.value = difficulty
        applyFilter()
    }

    fun onMaxCookingTimeMinutesChange(maxMinutes: Int?) {
        _maxCookingTimeMinutes.value = maxMinutes
        applyFilter()
    }

    fun getRecipeById(recipeId: Int) : Recipe? {
        return repository.getRecipeById(recipeId = recipeId)
    }

    fun clearFilter() {
        _selectedCategory.value = null
        _selectedDifficulty.value = null
        _maxCookingTimeMinutes.value = null

        applyFilter()
    }

    private fun applyFilter() {
        _recipes.value = repository.filterRecipes(
            query = _query.value,
            category = _selectedCategory.value,
            difficulty = _selectedDifficulty.value,
            maxCookingTimeMinutes = _maxCookingTimeMinutes.value
        )
    }

}