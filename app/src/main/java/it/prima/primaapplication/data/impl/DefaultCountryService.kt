package it.prima.primaapplication.data.impl

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import it.prima.primaapplication.data.CountriesQuery
import it.prima.primaapplication.data.CountryQuery
import it.prima.primaapplication.data.CountryService
import javax.inject.Inject

class DefaultCountryService @Inject constructor(
    private val apolloClient: ApolloClient
) : CountryService {

    override suspend fun getCountries(): Response<CountriesQuery.Data> {
        return apolloClient.query(CountriesQuery()).await()
    }

    override suspend fun getCountry(code: String): Response<CountryQuery.Data> {
        return apolloClient.query(CountryQuery(code)).await()
    }

}