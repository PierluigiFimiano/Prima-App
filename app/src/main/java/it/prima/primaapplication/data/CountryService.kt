package it.prima.primaapplication.data

import com.apollographql.apollo.api.Response
import it.prima.primaapplication.data.CountriesQuery
import it.prima.primaapplication.data.CountryQuery

interface CountryService {

    suspend fun getCountries(): Response<CountriesQuery.Data>

    suspend fun getCountry(code: String): Response<CountryQuery.Data>
}