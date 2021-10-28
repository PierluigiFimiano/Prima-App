package it.prima.primaapplication.data

interface CountryRepository {

    suspend fun getCountries(): Result<CountriesQuery.Data>

    suspend fun getCountry(code: String): Result<CountryQuery.Data>
}