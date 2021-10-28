package it.prima.primaapplication.data.impl

import it.prima.primaapplication.data.*
import javax.inject.Inject

class DefaultCountryRepository @Inject constructor(
    private val countryService: CountryService
) : CountryRepository {

    override suspend fun getCountries(): Result<CountriesQuery.Data> = apolloResponse {
        countryService.getCountries()
    }

    override suspend fun getCountry(code: String): Result<CountryQuery.Data> = apolloResponse {
        countryService.getCountry(code)
    }
}