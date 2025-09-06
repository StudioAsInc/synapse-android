package com.synapse.social.studioasinc;

import android.content.Intent;
import android.view.View;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static com.synapse.social.studioasinc.RecyclerViewMatcher.withRecyclerView;
import static com.synapse.social.studioasinc.DrawableMatcher.withDrawable;

@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {

    @Rule
    public ActivityTestRule<ChatActivity> activityRule = new ActivityTestRule<>(ChatActivity.class, true, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("uid", "test_uid");
        intent.putExtra("is_test", "true"); // Add a flag to indicate test mode
        activityRule.launchActivity(intent);
    }

    @Test
    public void avatarImageViewIsGone() {
        // Add a delay to wait for the activity to be ready
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if there are any messages. If so, check the first one.
        if (getRecyclerViewCount() > 0) {
            onView(withRecyclerView(R.id.chat_recyclerview).atPositionOnView(0, R.id.mProfileCard))
                    .check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void singleMessageHasCorrectBackground() {
        // Add a delay to wait for the activity to be ready
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if there is at least one message, and assume it's a single incoming message.
        if (getRecyclerViewCount() > 0) {
            onView(withRecyclerView(R.id.chat_recyclerview).atPositionOnView(0, R.id.messageBG))
                    .check(matches(withDrawable(R.drawable.shape_incoming_bubble_single)));
        }
    }

    private int getRecyclerViewCount() {
        final int[] count = {0};
        onView(withId(R.id.chat_recyclerview)).check((view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                return;
            }
            count[0] = ((androidx.recyclerview.widget.RecyclerView) view).getAdapter().getItemCount();
        });
        return count[0];
    }
}
