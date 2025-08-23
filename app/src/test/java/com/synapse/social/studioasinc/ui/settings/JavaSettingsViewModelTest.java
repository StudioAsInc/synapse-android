package com.synapse.social.studioasinc.ui.settings;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.synapse.social.studioasinc.data.settings.JavaSettingsRepository;

@RunWith(MockitoJUnitRunner.class)
public class JavaSettingsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private JavaSettingsRepository repository;

    private JavaSettingsViewModel viewModel;

    // Mock LiveData objects that will be "returned" by the repository
    private MutableLiveData<String> themeLiveData;
    private MutableLiveData<String> aiAssistantLiveData;

    @Before
    public void setUp() {
        // Initialize mock LiveData
        themeLiveData = new MutableLiveData<>("system");
        aiAssistantLiveData = new MutableLiveData<>("gemini");

        // Define the behavior of the mock repository
        when(repository.getTheme()).thenReturn(themeLiveData);
        when(repository.getAiAssistant()).thenReturn(aiAssistantLiveData);

        // Instantiate the ViewModel with the mock repository
        viewModel = new JavaSettingsViewModel(repository);
    }

    @Test
    public void setTheme_callsRepository() {
        String newTheme = "dark";
        viewModel.setTheme(newTheme);

        // Verify that the repository's setTheme method was called exactly once with "dark"
        verify(repository, times(1)).setTheme(newTheme);
    }

    @Test
    public void getTheme_observesRepositoryData() {
        // Check initial value
        assertEquals("system", viewModel.getTheme().getValue());

        // Simulate a change from the source (the repository's LiveData)
        themeLiveData.postValue("light");

        // Verify the ViewModel's LiveData reflects the change
        assertEquals("light", viewModel.getTheme().getValue());
    }

    @Test
    public void setAiAssistant_callsRepository() {
        String newAssistant = "claude";
        viewModel.setAiAssistant(newAssistant);

        // Verify the interaction
        verify(repository).setAiAssistant(newAssistant);
    }

    @Test
    public void getAiAssistant_observesRepositoryData() {
        // Check initial value
        assertEquals("gemini", viewModel.getAiAssistant().getValue());

        // Simulate a change
        aiAssistantLiveData.postValue("chatgpt");

        // Verify the change is reflected
        assertEquals("chatgpt", viewModel.getAiAssistant().getValue());
    }
}
