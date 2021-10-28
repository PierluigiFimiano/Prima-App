package it.prima.primaapplication.ui.country

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.prima.primaapplication.R
import it.prima.primaapplication.data.CountryQuery
import it.prima.primaapplication.ui.util.CollectEvent
import it.prima.primaapplication.util.buildList

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountryScreen() {
    val viewModel: CountryViewModel = hiltViewModel()

    val country = viewModel.country.collectAsState()
    val error = viewModel.errorEvent.collectAsState()
    val dataLoading = viewModel.dataLoading.collectAsState()

    val scaffoldState = rememberScaffoldState()

    val errorMessage = stringResource(R.string.error_message)
    CollectEvent(error) {
        scaffoldState.snackbarHostState.showSnackbar(
            message = errorMessage,
            duration = SnackbarDuration.Long
        )
    }

    Scaffold(scaffoldState = scaffoldState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = dataLoading.value,
                exit = fadeOut()
            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
            }

            AnimatedVisibility(
                visible = country.value != null,
                enter = fadeIn()
            ) {
                CountryDetails(
                    country = country.value!!,
                    modifier = Modifier
                        .matchParentSize()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CountryDetails(country: CountryQuery.Country, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = country.emoji,
            style = MaterialTheme.typography.h1
        )

        val name = stringResource(R.string.country_name)
        val code = stringResource(R.string.country_code)
        val phone = stringResource(R.string.country_phone)
        val languages = stringResource(R.string.country_languages)
        val capital = stringResource(R.string.country_capital)
        val currency = stringResource(R.string.country_currency)
        val continent = stringResource(R.string.country_continent)

        val details = remember(country) {
            buildList(
                name to country.name,
                code to country.code,
                continent to country.continent.name,
                phone to country.phone
            ) {
                country.capital?.also {
                    add(capital to it)
                }
                country.currency?.also {
                    add(currency to it)
                }
                if (country.languages.isNotEmpty()) {
                    val all = buildString {
                        country.languages.forEachIndexed { index, language ->
                            append(language.code)
                            if (index < country.languages.size - 1) {
                                append(", ")
                            }
                        }
                    }
                    add(languages to all)
                }
            }
        }

        DetailsView(
            details = details,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
        )
    }
}

@Composable
fun DetailsView(details: List<Pair<String, String>>, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        details.forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${pair.first}: ",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )

                Text(
                    text = pair.second,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}