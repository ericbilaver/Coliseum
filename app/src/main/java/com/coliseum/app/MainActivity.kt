package com.coliseum.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.moviebase.tmdb.Tmdb3
import com.coliseum.app.ui.screens.HomeScreen
import com.coliseum.app.ui.screens.MovieScreen
import com.coliseum.app.ui.theme.ColiseumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            ColiseumTheme {
                NavigationHoster()
            }
        }
    }
}

// Define the navigation routes as sealed class
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MovieDetails : Screen("movie/{movieId}") {
        fun createRoute(movieId: Int): String {
            return "movie/$movieId"
        }
    }
}

@Composable
private fun NavigationHoster() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        // Define your navigation routes here
        composable(Screen.Home.route) {
            Column {
                Text("Home")
                HomeScreen(
                    onClick = { movieId ->
                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                    }
                )
            }

        }
        composable(
            Screen.MovieDetails.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) {
            val movieId = it.arguments?.getInt("movieId") ?: 0
            Column {
                Text("Detail")
                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
                MovieScreen(movieId)
            }
        }
    }
}
object TmdbClient {
    private const val API_KEY = BuildConfig.TMDB_API_KEY
    val tmdb = Tmdb3(API_KEY)
}