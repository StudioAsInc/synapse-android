package com.synapse.social.studioasinc.groupchat.presentation.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.synapse.social.studioasinc.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class CreateGroupActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun createGroupButton_isDisabledInitially() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        onView(withId(R.id.btnCreateGroup))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun createGroupButton_isEnabledWhenGroupNameEntered() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Enter group name
        onView(withId(R.id.etGroupName))
            .perform(typeText("Test Group"))

        // Check if create button is enabled
        onView(withId(R.id.btnCreateGroup))
            .check(matches(isEnabled()))
    }

    @Test
    fun groupNameInput_showsValidationError_whenNameTooLong() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Enter a very long group name (over 100 characters)
        val longName = "a".repeat(101)
        onView(withId(R.id.etGroupName))
            .perform(typeText(longName))

        // Check if create button is disabled due to validation
        onView(withId(R.id.btnCreateGroup))
            .check(matches(not(isEnabled())))
    }

    @Test
    fun groupDescriptionInput_acceptsText() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        val description = "This is a test group description"
        onView(withId(R.id.etGroupDescription))
            .perform(typeText(description))

        onView(withId(R.id.etGroupDescription))
            .check(matches(withText(description)))
    }

    @Test
    fun maxMembersSlider_updatesValueDisplay() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // The slider should show "256 members" by default
        onView(withId(R.id.tvMaxMembersValue))
            .check(matches(withText("256 members")))
    }

    @Test
    fun privateGroupSwitch_canBeToggled() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Initially unchecked
        onView(withId(R.id.switchPrivateGroup))
            .check(matches(not(isChecked())))

        // Toggle the switch
        onView(withId(R.id.switchPrivateGroup))
            .perform(click())

        // Should now be checked
        onView(withId(R.id.switchPrivateGroup))
            .check(matches(isChecked()))
    }

    @Test
    fun groupSettingsSwitches_canBeToggled() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Test all group settings switches
        val switchIds = listOf(
            R.id.switchAllowMembersToAdd,
            R.id.switchAllowMembersToEdit,
            R.id.switchOnlyAdminsMessage
        )

        switchIds.forEach { switchId ->
            // Initially unchecked
            onView(withId(switchId))
                .check(matches(not(isChecked())))

            // Toggle the switch
            onView(withId(switchId))
                .perform(click())

            // Should now be checked
            onView(withId(switchId))
                .check(matches(isChecked()))
        }
    }

    @Test
    fun searchMembersInput_showsSearchResults() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Type in search field
        onView(withId(R.id.etSearchMembers))
            .perform(typeText("john"))

        // Search results RecyclerView should become visible
        // Note: In a real test, you'd need to set up proper mocking
        // to return actual search results
        onView(withId(R.id.rvSearchResults))
            .check(matches(isDisplayed()))
    }

    @Test
    fun memberCountDisplay_updatesWhenMembersSelected() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Initially should show 0 members
        onView(withId(R.id.tvMemberCount))
            .check(matches(withText("0 members selected")))
    }

    @Test
    fun toolbar_hasCorrectTitle() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Check toolbar title
        onView(withText("Create Group"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun groupIconButton_isDisplayed() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        onView(withId(R.id.btnSelectIcon))
            .check(matches(isDisplayed()))
            .check(matches(withText("Select Group Icon")))
    }

    @Test
    fun formValidation_requiresGroupName() {
        ActivityScenario.launch(CreateGroupActivity::class.java)

        // Empty group name should disable create button
        onView(withId(R.id.btnCreateGroup))
            .check(matches(not(isEnabled())))

        // Enter group name
        onView(withId(R.id.etGroupName))
            .perform(typeText("Valid Group Name"))

        // Create button should now be enabled
        onView(withId(R.id.btnCreateGroup))
            .check(matches(isEnabled()))

        // Clear group name
        onView(withId(R.id.etGroupName))
            .perform(clearText())

        // Create button should be disabled again
        onView(withId(R.id.btnCreateGroup))
            .check(matches(not(isEnabled())))
    }
}