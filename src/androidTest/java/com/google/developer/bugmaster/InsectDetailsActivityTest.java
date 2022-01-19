package com.google.developer.bugmaster;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.developer.bugmaster.data.Insect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by stoyan.yanev on 6.1.2018 г..
 */

@RunWith(AndroidJUnit4.class)
public class InsectDetailsActivityTest {

    private static final String TEST_COMMON_NAME = "testName";
    private static final String TEST_SCIENTIFIC_NAME = "testScientificName";
    private static final String TEST_CLASSIFICATION = "testClassification";
    private static final String TEST_IMAGE = "testImage";
    private static final int TEST_DANGER_LEVEL = 5;

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            InsectDetailsActivity.class, false, false);

    @Test
    public void checkInsectInformation() {

        Insect insect = new Insect(TEST_COMMON_NAME,
                TEST_SCIENTIFIC_NAME,
                TEST_CLASSIFICATION,
                TEST_IMAGE,
                TEST_DANGER_LEVEL);
        Intent intent = new Intent();
        intent.putExtra(InsectDetailsActivity.EXTRA_INSECT, insect);
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.activity_insect_details_common_name)).check(matches(withText(containsString(TEST_COMMON_NAME))));
        onView(withId(R.id.activity_insect_details_scientific_name)).check(matches(withText(containsString(TEST_SCIENTIFIC_NAME))));
        onView(withId(R.id.activity_insect_details_classification)).check(matches(withText(containsString(TEST_CLASSIFICATION))));
    }
}
