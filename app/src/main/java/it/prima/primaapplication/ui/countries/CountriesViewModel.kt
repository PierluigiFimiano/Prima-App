package it.prima.primaapplication.ui.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.prima.primaapplication.data.CountriesQuery
import it.prima.primaapplication.data.CountryRepository
import it.prima.primaapplication.data.Result
import it.prima.primaapplication.util.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dataLoading = MutableStateFlow(false)
    val dataLoading: StateFlow<Boolean> = _dataLoading

    private val _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _countries = MutableStateFlow<List<CountriesQuery.Country>>(emptyList())
    val countries: StateFlow<List<CountriesQuery.Country>> =
        _countries.combine(_filter) { countries, filter ->
            if (filter.isNotBlank()) {
                countries.filter { country ->
                    country.continent.name.contains(filter, ignoreCase = true) ||
                            country.languages.any { language ->
                                language.name?.contains(filter, ignoreCase = true) == true
                            }
                }
            } else {
                countries
            }
        }.stateIn(
            viewModelScope + dispatcher,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _errorEvent = MutableStateFlow<Event<Unit>?>(null)
    val errorEvent: StateFlow<Event<Unit>?> = _errorEvent

    init {
        _dataLoading.value = true
        viewModelScope.launch {
            val response: Result<CountriesQuery.Data> = countryRepository.getCountries()
            if (response is Result.Success) {
                _countries.value = response.data.countries
            } else {
                _errorEvent.value = Event(Unit)
            }

            _dataLoading.value = false
        }
    }

    fun filter(filter: String) {
        _filter.value = filter
    }

}