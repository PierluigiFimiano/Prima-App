package it.prima.primaapplication.data.impl

import com.apollographql.apollo.api.Response
import it.prima.primaapplication.data.CountriesQuery
import it.prima.primaapplication.data.CountryQuery
import it.prima.primaapplication.data.CountryService
import javax.inject.Inject

class FakeCountryService @Inject constructor() : CountryService {

    override suspend fun getCountries(): Response<CountriesQuery.Data> {
        return Response.builder<CountriesQuery.Data>(CountriesQuery())
            .data(
                CountriesQuery.Data(
                    countries = listOf(
                        CountriesQuery.Country(
                            code = "IT",
                            name = "Italy",
                            emoji = "it",
                            continent = CountriesQuery.Continent(
                                name = "Europe",
                                code = "eu"
                            ),
                            languages = listOf(
                                CountriesQuery.Language(
                                    code = "it",
                                    name = "Italian"
                                )
                            )
                        )
                    )
                )
            )
            .build()
    }

    override suspend fun getCountry(code: String): Response<CountryQuery.Data> {
        TODO("Not yet implemented")
    }
}