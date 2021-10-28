package it.prima.primaapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.prima.primaapplication.data.CountryService
import it.prima.primaapplication.data.impl.DefaultCountryService
import it.prima.primaapplication.data.CountryRepository
import it.prima.primaapplication.data.impl.DefaultCountryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Binds
    @Singleton
    abstract fun bindCountryRepository(repository: DefaultCountryRepository): CountryRepository

    @Binds
    @Singleton
    abstract fun bindCountryService(countryService: DefaultCountryService): CountryService
}