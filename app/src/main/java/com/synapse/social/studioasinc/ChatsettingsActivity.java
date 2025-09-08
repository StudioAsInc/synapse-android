package com.synapse.social.studioasinc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.synapse.social.studioasinc.adapter.SettingsAdapter;
import com.synapse.social.studioasinc.model.Setting;
import com.synapse.social.studioasinc.util.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatsettingsActivity extends AppCompatActivity implements SettingsAdapter.OnSettingChangedListener, SettingsAdapter.OnButtonClickListener {

    private SharedPreferences appSettings;
    private SettingsAdapter.PreviewViewHolder previewViewHolder;
    private List<Setting> settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatsettings);

        appSettings = getSharedPreferences("appSettings", Activity.MODE_PRIVATE);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        settings = createSettingsList();
        SettingsAdapter adapter = new SettingsAdapter(this, settings, appSettings, this, this);
        recyclerView.setAdapter(adapter);

        recyclerView.post(() -> {
            int previewPosition = -1;
            for (int i = 0; i < settings.size(); i++) {
                if (settings.get(i).getType() == Setting.Type.PREVIEW) {
                    previewPosition = i;
                    break;
                }
            }

            if (previewPosition != -1) {
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(previewPosition);
                if (holder instanceof SettingsAdapter.PreviewViewHolder) {
                    previewViewHolder = (SettingsAdapter.PreviewViewHolder) holder;
                    updatePreview();
                }
            }
        });
    }

    private List<Setting> createSettingsList() {
        List<Setting> settings = new ArrayList<>();

        settings.add(new Setting("Chat"));
        settings.add(new Setting(Setting.Type.SEEKBAR, "Message text size", "Adjusting this font size doesn't effects the whole app.", R.drawable.ic_text_fields_48px, appSettings.getInt("ChatTextSize", 16), 12, 30, "ChatTextSize"));
        settings.add(new Setting(Setting.Type.PREVIEW));
        settings.add(new Setting(Setting.Type.SEEKBAR, "Message corner round", "This doesn't affect other UI corners' radius", R.drawable.ic_rounded_corner, appSettings.getInt("ChatCornerRadius", 20), 0, 50, "ChatCornerRadius"));
        settings.add(new Setting(Setting.Type.BUTTON, "Background wallpapers", "Default", R.drawable.ic_photo_library_48px, "background_wallpapers"));
        settings.add(new Setting(Setting.Type.BUTTON, "Theme store", "Theme changes the whole apps overall interface, colours, fonts, and images as well", R.drawable.ic_category, "theme_store"));

        settings.add(new Setting("Media and sound"));
        settings.add(new Setting(Setting.Type.SWITCH, "Tap to show next media", "Tap near the edge of the screen while viewing media to navigate", R.drawable.ic_touch, appSettings.getBoolean("tap_to_show_next_media", true), "tap_to_show_next_media"));
        settings.add(new Setting(Setting.Type.SWITCH, "Rise to Listen", "Switch sound to the earpiece by raising the phone to your ear", R.drawable.ic_lift_device, appSettings.getBoolean("rise_to_listen", true), "rise_to_listen"));
        settings.add(new Setting(Setting.Type.SWITCH, "Pause music while recording", "Pause music when you start recording a video message", R.drawable.ic_music_off, appSettings.getBoolean("pause_music_while_recording", true), "pause_music_while_recording"));

        settings.add(new Setting("Security and services"));
        settings.add(new Setting(Setting.Type.SWITCH, "App lock", "", R.drawable.ic_lock, appSettings.getBoolean("app_lock", false), "app_lock"));
        settings.add(new Setting(Setting.Type.SWITCH, "Keep-alive service", "Keep services running in background.", R.drawable.ic_component_exchange, appSettings.getBoolean("keep_alive_service", true), "keep_alive_service"));
        settings.add(new Setting(Setting.Type.SWITCH, "Background connection", "Keep processing with low power", R.drawable.tab_inactive_24px, appSettings.getBoolean("background_connection", false), "background_connection"));
        settings.add(new Setting(Setting.Type.BUTTON, "Theme", "Default", R.drawable.ic_category, "theme"));

        settings.add(new Setting("App theming"));
        settings.add(new Setting(Setting.Type.BUTTON, "Language", "English (International)", R.drawable.language_chinese_array_24px, "language"));

        settings.add(new Setting("Storage and network"));
        settings.add(new Setting(Setting.Type.BUTTON, "Storage uses", "Used 1% of your device storage", R.drawable.data_table_24px, "storage_uses"));
        settings.add(new Setting(Setting.Type.BUTTON, "Data uses", "This data has been recorded since\nlast month", R.drawable.data_usage_24px, "data_uses"));
        settings.add(new Setting(Setting.Type.SWITCH, "Streming video", "Stream video without downloading", R.drawable.cast_24px, appSettings.getBoolean("streaming_video", false), "streaming_video"));

        settings.add(new Setting("AI and automations"));
        settings.add(new Setting(Setting.Type.BUTTON, "Synapse Premium", "", R.drawable.star_shine_24px, "synapse_premium"));
        settings.add(new Setting(Setting.Type.BUTTON, "Buy Credits", "", R.drawable.credit_card_heart_24px, "buy_credits"));
        settings.add(new Setting(Setting.Type.BUTTON, "AI Settings", "Setup your artificial intelligence settings", R.drawable.hand_package_24px, "ai_settings"));


        return settings;
    }

    private void updatePreview() {
        if (previewViewHolder == null) {
            return;
        }

        int textSize = appSettings.getInt("ChatTextSize", 16);
        int cornerRadius = appSettings.getInt("ChatCornerRadius", 20);

        previewViewHolder.messageText.setTextSize(textSize);
        previewViewHolder.txtMsg1.setTextSize(textSize);
        previewViewHolder.repliedMessage.setTextSize(textSize);

        GradientDrawable messageBgDrawable = new GradientDrawable();
        messageBgDrawable.setShape(GradientDrawable.RECTANGLE);
        messageBgDrawable.setColor(ThemeUtils.getThemeColor(this, R.attr.colorSurfaceVariant));
        messageBgDrawable.setCornerRadius(cornerRadius);
        previewViewHolder.messageBG.setBackground(messageBgDrawable);

        GradientDrawable messageBg1Drawable = new GradientDrawable();
        messageBg1Drawable.setShape(GradientDrawable.RECTANGLE);
        messageBg1Drawable.setColor(ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        messageBg1Drawable.setCornerRadius(cornerRadius);
        previewViewHolder.messageBG1.setBackground(messageBg1Drawable);
    }

    @Override
    public void onSettingChanged(String key, int value) {
        if (key.equals("ChatTextSize") || key.equals("ChatCornerRadius")) {
            updatePreview();
        }
    }

    @Override
    public void onButtonClick(String key) {
        switch (key) {
            case "background_wallpapers":
                startActivity(new Intent(this, BgWallpapersActivity.class));
                break;
            case "theme_store":
                Toast.makeText(this, "Theme store clicked", Toast.LENGTH_SHORT).show();
                break;
            case "theme":
                Toast.makeText(this, "Theme clicked", Toast.LENGTH_SHORT).show();
                break;
            case "language":
                Toast.makeText(this, "Language clicked", Toast.LENGTH_SHORT).show();
                break;
            case "storage_uses":
                Toast.makeText(this, "Storage uses clicked", Toast.LENGTH_SHORT).show();
                break;
            case "data_uses":
                Toast.makeText(this, "Data uses clicked", Toast.LENGTH_SHORT).show();
                break;
            case "synapse_premium":
                startActivity(new Intent(this, LabsActivity.class));
                break;
            case "buy_credits":
                Toast.makeText(this, "Buy credits clicked", Toast.LENGTH_SHORT).show();
                break;
            case "ai_settings":
                Toast.makeText(this, "AI settings clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (previewViewHolder != null) {
            updatePreview();
        }
    }
}
