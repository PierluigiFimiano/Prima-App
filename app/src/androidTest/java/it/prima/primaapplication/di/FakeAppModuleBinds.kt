package it.prima.primaapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import it.prima.primaapplication.data.CountryRepository
import it.prima.primaapplication.data.CountryService
import it.prima.primaapplication.data.impl.DefaultCountryRepository
import it.prima.primaapplication.data.impl.FakeCountryService
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModuleBinds::class]
)
abstract class FakeAppModuleBinds {

    @Binds
    @Singleton
    abstract fun bindCountryService(countryService: FakeCountryService): CountryService

    @Binds
    @Singleton
    abstract fun bindCountryRepository(repository: DefaultCountryRepository): CountryRepository

}