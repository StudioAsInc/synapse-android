package ui.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.color.DynamicColors;
import your.package.name.R;

public class EditProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DynamicColors.applyToActivityIfAvailable(this);
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.simple_list_item_1);
        setTitle(getString(R.string.pref_title_edit_profile));
    }
}

