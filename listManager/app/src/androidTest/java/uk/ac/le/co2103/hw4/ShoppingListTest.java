package uk.ac.le.co2103.hw4;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.Root;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.core.internal.deps.guava.collect.Ordering;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager.widget.ViewPager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.le.co2103.hw4.Database.ShoppingListDB;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingListDao;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ShoppingListTest {

    static ShoppingListDao shoppingListDao;
    private ShoppingListDB db;
    private View decorView;

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        Context application = ApplicationProvider.getApplicationContext();
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        shoppingListDao = db.shoppingListDao();
        shoppingListDao.deleteAll();
    }

    @After
    public void teardown() {
        shoppingListDao.deleteAll();
    }

    @Test()
    public void testAddNewList() {
        ViewInteraction floatingActionButton = onView(withId(R.id.fab));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_new_item));
        appCompatEditText.perform(replaceText("Birthday Party"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withText("Create"));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteList() {
        ViewInteraction floatingActionButton = onView(withId(R.id.fab));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_new_item));
        appCompatEditText.perform(replaceText("Birthday Party"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withText("Create"));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(longClick());

        ViewInteraction materialButton2 = onView(withId(android.R.id.button1));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView2.check(doesNotExist());
    }

    @Test
    public void testAddProduct() {
        ViewInteraction floatingActionButton = onView(withId(R.id.fab));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_new_item));
        appCompatEditText.perform(replaceText("Birthday Party"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withText("Create"));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(click());

        ViewInteraction floatingActionButton2 = onView(withId(R.id.fabAddProduct));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.editTextName));
        appCompatEditText2.perform(replaceText("Cake"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.editTextQuantity));
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner),
                        childAtPosition(
                                allOf(withContentDescription("Enter Product Details..."),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction materialButton2 = onView(withText("Add"));
        materialButton2.perform(click());

        ViewInteraction textView2 = onView(withId(R.id.textViewName));
        textView2.check(matches(withText("Cake")));
        ViewInteraction textView3 = onView(withId(R.id.textViewQuantity));
        textView3.check(matches(withText("1")));
        ViewInteraction textView4 = onView(withId(R.id.textViewUnit));
        textView4.check(matches(withText("Unit")));
    }

    @Test
    public void testAddDuplicateProduct() {
        ViewInteraction floatingActionButton = onView(withId(R.id.fab));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_new_item));
        appCompatEditText.perform(replaceText("Birthday Party"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withText("Create"));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(click());

        ViewInteraction floatingActionButton2 = onView(withId(R.id.fabAddProduct));
        floatingActionButton2.perform(click());

         ViewInteraction appCompatEditText2 = onView(withId(R.id.editTextName));
        appCompatEditText2.perform(replaceText("Cake"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.editTextQuantity));
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner),
                        childAtPosition(
                                allOf(withContentDescription("Enter Product Details..."),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction materialButton2 = onView(withText("Add"));
        materialButton2.perform(click());

        floatingActionButton2 = onView(withId(R.id.fabAddProduct));
        floatingActionButton2.perform(click());

        appCompatEditText2 = onView(withId(R.id.editTextName));
        appCompatEditText2.perform(replaceText("Cake"), closeSoftKeyboard());

        appCompatEditText3 = onView(withId(R.id.editTextQuantity));
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard());

        appCompatSpinner = onView(
                allOf(withId(R.id.spinner),
                        childAtPosition(
                                allOf(withContentDescription("Enter Product Details..."),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        materialButton2 = onView(withText("Add"));
        materialButton2.perform(click());

        rule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                decorView = activity.getWindow().getDecorView();
            }
        });
        onView(withText("Product already exists"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testEditProduct() {
        ViewInteraction floatingActionButton = onView(withId(R.id.fab));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_new_item));
        appCompatEditText.perform(replaceText("Birthday Party"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withText("Create"));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(click());

        ViewInteraction floatingActionButton2 = onView(withId(R.id.fabAddProduct));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.editTextName));
        appCompatEditText2.perform(replaceText("Cake"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.editTextQuantity));
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner),
                        childAtPosition(
                                allOf(withContentDescription("Enter Product Details..."),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction materialButton2 = onView(withText("Add"));
        materialButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewName), withText("Cake"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview_products)))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        textView2.perform(click());

        ViewInteraction materialButton3 = onView(withId(android.R.id.button2));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(withId(R.id.button_plus));
        materialButton4.perform(click());
        materialButton4.perform(click());

        ViewInteraction materialButton5 = onView(withText(R.string.button_save));
        materialButton5.perform(click());

        ViewInteraction textView3 = onView(withId(R.id.textViewQuantity));
        textView3.check(matches(withText("3")));
    }

    @Test
    public void testDeleteProduct() {
        ViewInteraction floatingActionButton = onView(withId(R.id.fab));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(withId(R.id.edit_new_item));
        appCompatEditText.perform(replaceText("Birthday Party"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withText("Create"));
        materialButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewName), withText("Birthday Party"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        textView.perform(click());

        ViewInteraction floatingActionButton2 = onView(withId(R.id.fabAddProduct));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(withId(R.id.editTextName));
        appCompatEditText2.perform(replaceText("Cake"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(withId(R.id.editTextQuantity));
        appCompatEditText3.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner),
                        childAtPosition(
                                allOf(withContentDescription("Enter Product Details..."),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction materialButton2 = onView(withText("Add"));
        materialButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewName), withText("Cake"),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.recyclerview_products)))),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        textView2.perform(click());

        ViewInteraction materialButton3 = onView(withId(android.R.id.button1));
        materialButton3.perform(scrollTo(), click());

        textView2.check(doesNotExist());
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}