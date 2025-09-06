package com.synapse.social.studioasinc;

import android.view.View;
import android.graphics.drawable.Drawable;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher extends TypeSafeMatcher<View> {

    private final int expectedId;
    String resourceName;

    public DrawableMatcher(int expectedId) {
        super(View.class);
        this.expectedId = expectedId;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (target == null) {
            return false;
        }
        Drawable expectedDrawable = target.getContext().getResources().getDrawable(expectedId, null);
        resourceName = target.getContext().getResources().getResourceEntryName(expectedId);

        Drawable background = target.getBackground();
        if (background == null) {
            return false;
        }

        return background.getConstantState().equals(expectedDrawable.getConstantState());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with background from resource id: ");
        description.appendValue(expectedId);
        if (resourceName != null) {
            description.appendText("[");
            description.appendText(resourceName);
            description.appendText("]");
        }
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }
}
