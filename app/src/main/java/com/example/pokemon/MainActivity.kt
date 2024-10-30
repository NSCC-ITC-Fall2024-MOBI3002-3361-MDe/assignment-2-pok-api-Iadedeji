package com.example.pokemon

import PokemonViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokemon.ui.theme.PokemonTheme


class MainActivity : ComponentActivity() {
    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokemonSearchScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun PokemonSearchScreen(viewModel: PokemonViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pokémon",
            fontSize = 28.sp,
            color = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it.lowercase() },
            label = { Text("Enter Pokémon name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.fetchPokemon(searchQuery) }) {
            Text(text = "Go")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = viewModel.pokemonState.collectAsState().value) {
            is PokemonViewModel.PokemonState.Loading -> {
                CircularProgressIndicator()
            }
            is PokemonViewModel.PokemonState.Success -> {
                PokemonInfoDisplay(
                    name = state.pokemon.name,
                    height = state.pokemon.height,
                    weight = state.pokemon.weight,
                    types = state.pokemon.types.joinToString { it.type.name },
                    imageUrl = state.pokemon.sprites.frontDefault
                )
            }
            is PokemonViewModel.PokemonState.Error -> {
                Text(text = "Error: ${state.message}", color = Color.Red)
            }
            is PokemonViewModel.PokemonState.Empty -> {
                Text(text = "Enter a Pokémon name to begin.")
            }

            else -> {}
        }
    }
}

@Composable
fun PokemonInfoDisplay(name: String, height: Int, weight: Int, types: String, imageUrl: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = name,
                modifier = Modifier.size(128.dp)
            )
        }

        Text(text = name.capitalize(), fontSize = 24.sp, color = Color.Black)
        Text(text = "Types: $types", fontSize = 16.sp)
        Text(text = "Height: ${height / 10.0}m", fontSize = 16.sp)
        Text(text = "Weight: ${weight / 10.0}kg", fontSize = 16.sp)
    }
}
// https://www.youtube.com/watch?v=v0of23TxIKc&list=PLQkwcJG4YTCTimTCpEL5FZgaWdIZQuB7m
//youtube video for help and debugging