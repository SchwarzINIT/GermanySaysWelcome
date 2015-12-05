package com.github.socialc0de.gsw.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.socialc0de.gsw.R;
import com.github.socialc0de.gsw.fragments.DashboardFragment;
import com.github.socialc0de.gsw.fragments.EmergencyFragment;
import com.github.socialc0de.gsw.fragments.FaqFragment;
import com.github.socialc0de.gsw.fragments.MapFragment;
import com.github.socialc0de.gsw.fragments.PhraseFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private Drawer mDrawer;
    private AccountHeader headerResult;
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private SharedPreferences mPrefs;
    private final String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check if app was started before
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
        String languageSetting = mPrefs.getString("languageSetting", "NOTHING");

        String asylumStep = mPrefs.getString("asylumStep", "");
        Log.d("LanguageSetting: " + languageSetting, "AsylumStep: " + asylumStep);

        Log.d("[MainActivity] ", "Location: " + getResources().getConfiguration().locale.getLanguage());

        if (!welcomeScreenShown) {
            // here you can launch another activity if you like
            // the code below will display a popup

            Intent myIntent = new Intent(MainActivity.this, SetupActivity.class);
            MainActivity.this.startActivity(myIntent);

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit(); // Very important to save the preference
        }

        new MaterialDialog.Builder(this)
                .title("Information")
                .content("SetupActivity is only shown at the very first startup of the app. Here you have the chance to open our setup again [DEVELOPER-OPTION]")
                .positiveText("Open Setup")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        Intent myIntent = new Intent(MainActivity.this, SetupActivity.class);
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .negativeText("Close")
                .show();


        fragmentList.add(new DashboardFragment());
        fragmentList.add(new FaqFragment());
        fragmentList.add(new EmergencyFragment());
        fragmentList.add(new PhraseFragment());
        fragmentList.add(new MapFragment());


        createNavigationDrawer(mDrawer);
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new com.github.socialc0de.gsw.TypefaceSpan(getApplicationContext(), "bebaskai.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        // Toolbar Initializing
        final Toolbar toolbar = ((Toolbar) findViewById(R.id.app_bar));
        toolbar.setTitle(s);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.canShowOverflowMenu();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                toolbar.showOverflowMenu();
                Log.d("Toolbar Menu clicked", "");
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentList.get(0)).commit();


    }

    private void createNavigationDrawer(Drawer mDrawer) {
        if (mDrawer == null) {
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.productback)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .withSelectionListEnabledForSingleProfile(false)
                    .build();

            this.mDrawer = new DrawerBuilder()
                    .withActivity(this)
                    .withToolbar((Toolbar) findViewById(R.id.app_bar))
                    .withActionBarDrawerToggle(true)
                    .withAccountHeader(headerResult)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(getResources().getString(R.string.dashboard)),
                            new PrimaryDrawerItem().withName(getResources().getString(R.string.faq)),
                            new PrimaryDrawerItem().withName("Emergency"),
                            new PrimaryDrawerItem().withName(getResources().getString(R.string.phrasebook)),
                            new PrimaryDrawerItem().withName(getResources().getString(R.string.map)))
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //Log.d("onItemClick called: ", "position: "+position);
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentList.get(position - 1)).addToBackStack(null).commit();
                            return false;
                        }
                    })
                    .build();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
