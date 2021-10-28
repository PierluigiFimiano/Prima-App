package it.prima.primaapplication.ui.countries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.prima.primaapplication.MainCoroutineRule
import it.prima.primaapplication.data.CountryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
@HiltAndroidTest
class CountriesViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var countryRepository: CountryRepository

    @Before
    fun setup() {
        hiltAndroidRule.inject()
    }

    @Test
    fun getAndFilterCountriesTest(): Unit = runBlockingTest(mainCoroutineRule.coroutineContext) {

        val viewModel = CountriesViewModel(
            countryRepository,
            coroutineContext[ContinuationInterceptor] as CoroutineDispatcher
        )

        val job = launch {
            viewModel.countries.collect()
        }

        Truth.assertThat(viewModel.dataLoading.value).isFalse()
        Truth.assertThat(viewModel.errorEvent.value).isNull()

        runCurrent()

        Truth.assertThat(viewModel.countries.value).isNotEmpty()
        Truth.assertThat(viewModel.filter.value).isEqualTo("")

        viewModel.filter("en")

        runCurrent()

        Truth.assertThat(viewModel.countries.value).isEmpty()
        Truth.assertThat(viewModel.filter.value).isEqualTo("en")

        job.cancel()
    }


}