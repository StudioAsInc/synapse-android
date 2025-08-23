package com.synapse.social.studioasinc;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.synapse.social.studioasinc.databinding.SettingsBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    private SettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // The Fragment is added via the XML layout's `android:name` attribute.
        // This Activity now correctly serves as a Hilt entry point and a container for the UI.
    }
}
