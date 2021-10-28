package it.prima.primaapplication.ui.country

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.prima.primaapplication.data.CountryQuery
import it.prima.primaapplication.data.CountryRepository
import it.prima.primaapplication.data.Result
import it.prima.primaapplication.util.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val _country = MutableStateFlow<CountryQuery.Country?>(null)
    val country: StateFlow<CountryQuery.Country?> = _country

    private val _dataLoading = MutableStateFlow(false)
    val dataLoading: StateFlow<Boolean> = _dataLoading

    private val _errorEvent = MutableStateFlow<Event<Unit>?>(null)
    val errorEvent: StateFlow<Event<Unit>?> = _errorEvent

    init {
        _dataLoading.value = true
        viewModelScope.launch {
            val response = countryRepository.getCountry(handle["code"]!!)
            if (response is Result.Success) {
                _country.value = response.data.country
            } else {
                _errorEvent.value = Event(Unit)
            }

            _dataLoading.value = false
        }
    }
}