package it.prima.primaapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import it.prima.primaapplication.R
import it.prima.primaapplication.ui.countries.CountriesScreen
import it.prima.primaapplication.ui.country.CountryScreen
import it.prima.primaapplication.ui.theme.PrimaTheme
import it.prima.primaapplication.ui.util.navigate

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimaApp()
        }
    }
}

object PrimaDestinations {
    const val COUNTRIES_LIST = "countries"
    const val COUNTRY_DETAILS = "countries/{code}"
}

class PrimaNavigationActions(navController: NavController) {
    val navigateToCountryDetails: (code: String) -> Unit = { code ->
        navController.navigate(PrimaDestinations.COUNTRY_DETAILS, code) {
            popUpTo(PrimaDestinations.COUNTRIES_LIST) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PrimaApp() {
    PrimaTheme {
        Scaffold(
            topBar = { TopBar() }
        ) {
            val navController = rememberAnimatedNavController()
            val navActions = remember { PrimaNavigationActions(navController) }

            AnimatedNavHost(
                navController = navController,
                startDestination = PrimaDestinations.COUNTRIES_LIST
            ) {
                composable(
                    route = PrimaDestinations.COUNTRIES_LIST,
                    popEnterTransition = { _, _ ->
                        slideInHorizontally() + fadeIn()
                    },
                    exitTransition = { _, _ ->
                        slideOutHorizontally() + fadeOut()
                    }
                ) {
                    CountriesScreen(navActions.navigateToCountryDetails)
                }

                composable(
                    route = PrimaDestinations.COUNTRY_DETAILS,
                    enterTransition = { _, _ ->
                        slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn()
                    },
                    popExitTransition = { _, _ ->
                        slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
                    }
                ) {
                    CountryScreen()
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        }
    )
}