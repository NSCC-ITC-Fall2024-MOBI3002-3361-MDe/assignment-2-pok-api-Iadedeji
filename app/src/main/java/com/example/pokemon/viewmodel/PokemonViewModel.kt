import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    // Define a sealed class to handle UI states
    sealed class PokemonState {
        object Loading : PokemonState()
        data class Success(val pokemon: Pokemon) : PokemonState()
        data class Error(val message: String) : PokemonState()
        object Empty : PokemonState()
    }

    private val _pokemonState = MutableStateFlow<PokemonState>(PokemonState.Empty)
    val pokemonState: StateFlow<PokemonState> = _pokemonState

    fun fetchPokemon(name: String) {
        if (name.isBlank()) return

        // Set the state to loading when starting the fetch
        _pokemonState.value = PokemonState.Loading

        viewModelScope.launch {
            try {
                // Perform the API call
                val pokemon = RetrofitInstance.api.getPokemon(name.lowercase())
                _pokemonState.value = PokemonState.Success(pokemon)
            } catch (e: Exception) {
                // Set error state with a message in case of failure
                _pokemonState.value = PokemonState.Error("Failed to fetch Pokémon data.")
            }
        }
    }
}
