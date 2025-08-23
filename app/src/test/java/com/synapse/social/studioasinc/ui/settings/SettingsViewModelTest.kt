package com.synapse.social.studioasinc.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.synapse.social.studioasinc.data.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SettingsViewModel
    private lateinit var repository: SettingsRepository

    // Mock data flows that the repository will expose
    private val themeFlow = MutableStateFlow("system")
    private val aiAssistantFlow = MutableStateFlow("gemini")
    private val privateAccountFlow = MutableStateFlow(false)


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()

        // Whenever the repository's flows are accessed, return our mock flows
        whenever(repository.theme).thenReturn(themeFlow)
        whenever(repository.aiAssistant).thenReturn(aiAssistantFlow)
        whenever(repository.privateAccount).thenReturn(privateAccountFlow)

        viewModel = SettingsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setTheme calls repository with correct value`() = runTest {
        val newTheme = "dark"
        viewModel.setTheme(newTheme)
        // Ensure the launched coroutine completes
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the repository's method was called
        verify(repository).setTheme(newTheme)
    }

    @Test
    fun `theme StateFlow correctly reflects repository flow`() = runTest {
        viewModel.theme.test {
            // The initial value from the repository's flow
            assertEquals("system", awaitItem())

            // Simulate the repository's flow emitting a new value
            themeFlow.value = "light"
            assertEquals("light", awaitItem())

            // Simulate another change
            themeFlow.value = "dark"
            assertEquals("dark", awaitItem())
        }
    }

    @Test
    fun `setAiAssistant calls repository with correct value`() = runTest {
        val newAssistant = "claude"
        viewModel.setAiAssistant(newAssistant)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).setAiAssistant(newAssistant)
    }

    @Test
    fun `aiAssistant StateFlow correctly reflects repository flow`() = runTest {
        viewModel.aiAssistant.test {
            assertEquals("gemini", awaitItem())

            aiAssistantFlow.value = "chatgpt"
            assertEquals("chatgpt", awaitItem())
        }
    }
}
