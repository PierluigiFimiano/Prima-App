package it.prima.primaapplication.ui.countries

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import it.prima.primaapplication.R
import it.prima.primaapplication.data.CountriesQuery
import it.prima.primaapplication.ui.components.CollapsingToolbarLayout
import it.prima.primaapplication.ui.components.rememberCollapsingToolbarState
import it.prima.primaapplication.ui.util.CollectEvent

@Composable
fun CountriesScreen(navigateToCountry: (String) -> Unit) {
    val viewModel: CountriesViewModel = hiltViewModel()

    val countries by viewModel.countries.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val dataLoading by viewModel.dataLoading.collectAsState()
    val error = viewModel.errorEvent.collectAsState()

    val scaffoldState = rememberScaffoldState()

    val errorMessage = stringResource(R.string.error_message)
    CollectEvent(error) {
        scaffoldState.snackbarHostState.showSnackbar(
            message = errorMessage,
            duration = SnackbarDuration.Long
        )
    }

    val collapsingState = rememberCollapsingToolbarState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) {
        CollapsingToolbarLayout(
            modifier = Modifier.fillMaxSize(),
            toolBar = {
                SearchView(filter) { filter ->
                    viewModel.filter(filter)
                }
            },
            state = collapsingState
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(dataLoading),
                onRefresh = { },
                swipeEnabled = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                CountriesList(
                    countries = countries,
                    onClickCountry = navigateToCountry,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        top = with(LocalDensity.current) {
                            collapsingState.toolbarHeight.toDp()
                        }
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchView(value: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.padding(8.dp),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(text = stringResource(id = R.string.search_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = {
                        onValueChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onPrimary,
            trailingIconColor = MaterialTheme.colors.onPrimary,
            leadingIconColor = MaterialTheme.colors.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = MaterialTheme.colors.onPrimary.copy(ContentAlpha.medium)
        ),
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
    )
}

@Composable
fun CountriesList(
    countries: List<CountriesQuery.Country>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClickCountry: ((String) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(
            key = { country ->
                country.code
            },
            items = countries
        ) { country ->
            CountryListItem(
                country = country,
                onClickCountry = onClickCountry
            )
        }
    }
}

@Composable
fun CountryListItem(
    country: CountriesQuery.Country,
    onClickCountry: ((String) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClickCountry != null) {
                onClickCountry!!(country.code)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = country.emoji,
            style = MaterialTheme.typography.h5
        )
        Text(
            text = country.name,
            modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
        )
    }
}