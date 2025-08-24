package data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

public class SettingsRepositoryImpl implements SettingsRepository {

    private static final String PREFS_NAME = "settings_prefs";
    private static final String KEY_PRIVATE = "privacy_private_account";
    private static final String KEY_PUSH = "notif_enabled";
    private static final String KEY_MENTIONS = "notif_mentions";
    private static final String KEY_LIKES = "notif_likes";
    private static final String KEY_COMMENTS = "notif_comments";
    private static final String KEY_FOLLOWS = "notif_follows";
    private static final String KEY_THEME = "appearance_theme";
    private static final String KEY_FONT = "appearance_font_size";
    private static final String KEY_PROFILE_VIS = "privacy_profile_visibility";

    private final SharedPreferences prefs;

    public SettingsRepositoryImpl(@NonNull Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isPrivateAccount() {
        return prefs.getBoolean(KEY_PRIVATE, false);
    }

    @Override
    public void setPrivateAccount(boolean enabled) {
        prefs.edit().putBoolean(KEY_PRIVATE, enabled).apply();
    }

    @Override
    public boolean isPushNotificationsEnabled() {
        return prefs.getBoolean(KEY_PUSH, true);
    }

    @Override
    public void setPushNotificationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_PUSH, enabled).apply();
    }

    @Override
    public boolean isMentionsEnabled() {
        return prefs.getBoolean(KEY_MENTIONS, true);
    }

    @Override
    public void setMentionsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_MENTIONS, enabled).apply();
    }

    @Override
    public boolean isLikesEnabled() {
        return prefs.getBoolean(KEY_LIKES, true);
    }

    @Override
    public void setLikesEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_LIKES, enabled).apply();
    }

    @Override
    public boolean isCommentsEnabled() {
        return prefs.getBoolean(KEY_COMMENTS, true);
    }

    @Override
    public void setCommentsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_COMMENTS, enabled).apply();
    }

    @Override
    public boolean isFollowsEnabled() {
        return prefs.getBoolean(KEY_FOLLOWS, true);
    }

    @Override
    public void setFollowsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_FOLLOWS, enabled).apply();
    }

    @Override
    public String getThemeMode() {
        return prefs.getString(KEY_THEME, "system");
    }

    @Override
    public void setThemeMode(String mode) {
        prefs.edit().putString(KEY_THEME, mode).apply();
    }

    @Override
    public String getFontSize() {
        return prefs.getString(KEY_FONT, "normal");
    }

    @Override
    public void setFontSize(String size) {
        prefs.edit().putString(KEY_FONT, size).apply();
    }

    @Override
    public String getProfileVisibility() {
        return prefs.getString(KEY_PROFILE_VIS, "public");
    }

    @Override
    public void setProfileVisibility(String visibility) {
        prefs.edit().putString(KEY_PROFILE_VIS, visibility).apply();
    }
}

