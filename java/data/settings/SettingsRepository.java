package data.settings;

public interface SettingsRepository {

    boolean isPrivateAccount();
    void setPrivateAccount(boolean enabled);

    boolean isPushNotificationsEnabled();
    void setPushNotificationsEnabled(boolean enabled);

    boolean isMentionsEnabled();
    void setMentionsEnabled(boolean enabled);

    boolean isLikesEnabled();
    void setLikesEnabled(boolean enabled);

    boolean isCommentsEnabled();
    void setCommentsEnabled(boolean enabled);

    boolean isFollowsEnabled();
    void setFollowsEnabled(boolean enabled);

    String getThemeMode(); // system | light | dark
    void setThemeMode(String mode);

    String getFontSize(); // small | normal | large
    void setFontSize(String size);

    String getProfileVisibility(); // public | followers
    void setProfileVisibility(String visibility);
}

