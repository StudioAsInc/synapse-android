package com.synapse.social.studioasinc.ui.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.synapse.social.studioasinc.databinding.ActivityJavaSettingsBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class JavaSettingsActivity extends AppCompatActivity {

    private ActivityJavaSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJavaSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
