package ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import your.package.name.R

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
        // This is a stub screen. Replace with your real edit profile UI.
        setContentView(android.R.layout.simple_list_item_1)
        title = getString(R.string.pref_title_edit_profile)
    }
}

