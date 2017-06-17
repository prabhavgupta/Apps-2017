package com.example.arturrinkis.universitystudentrating;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import static android.view.View.GONE;

public class ProfessorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String ROOT_FRAGMENT_TAG = "ROOT_FRAGMENT_TAG";
    private String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT_TAG";
    private String ROOT_FOR_INSTRUCTION = "ROOT_FOR_INSTRUCTION";

    private ProfessorProfileFragment professorProfileFragment = new ProfessorProfileFragment();
    private ProfessorDisciplinesFragment professorDisciplinesFragment  = new ProfessorDisciplinesFragment();
    private SettingSearchFragment settingSearchFragment = new SettingSearchFragment();
    private IServerAPIManager serverAPIManager = null;
    private InstuctionFragment instuctionFragment = new InstuctionFragment();

    private LogoutAsyncTask logoutAsyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverAPIManager = new ServerAPIManager(getApplicationContext());
        setContentView(R.layout.activity_professor);
        if(savedInstanceState == null) {
            hideAppBarLayoutControl();
            hideContainer();
            initTabLayoutControl();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        initDrawerLayoutControl();

        if(savedInstanceState == null) {
            showProfileFragment();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        savedInstanceState.putInt("tabPosition", tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int tabPosition = savedInstanceState.getInt("tabPosition", 0);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
        tab.select();
        initTabLayoutControl();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initTabLayoutControl(){
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                tabLayout.setVisibility(View.VISIBLE);
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(ROOT_FRAGMENT_TAG);
                switch (tab.getPosition()) {
                    case 0:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                        fragmentTransaction.replace(R.id.tab_container, professorProfileFragment);
                        break;
                    case 1:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                        fragmentTransaction.replace(R.id.tab_container, professorDisciplinesFragment);
                        break;
                    case 2:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        fragmentTransaction.replace(R.id.tab_container, settingSearchFragment);
                        break;
                }

                fragmentTransaction.commit();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initDrawerLayoutControl(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    protected void showAbout() {
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
        int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.about_colored_small);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view branch_item clicks here.
        //  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_about) {
            showAbout();
        } else if(id == R.id.nav_exit) {
            logoutAsyncTask = new LogoutAsyncTask();
            logoutAsyncTask.execute();
        }
        else if(id == R.id.nav_instruction){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.tab_container, instuctionFragment);
            fragmentTransaction.addToBackStack(ROOT_FOR_INSTRUCTION);
            fragmentTransaction.commit();
        }
        else if(id == R.id.nav_topuniversities){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.topuniversities.com/university-rankings"));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProfileFragment(){
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tab_container, professorProfileFragment);
        fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    private void handleLogoutResult(HttpResponse httpResponse){
        if (httpResponse == null) {
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        } else if (httpResponse.getCode() == 200) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            ProfileCache.resetInstance();
        } else {
            Toast.makeText(this, "Internal server error", Toast.LENGTH_LONG).show();
        }
    }

    private void hideAppBarLayoutControl(){
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.setVisibility(GONE);
    }

    private void hideContainer(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);
        frameLayout.setVisibility(GONE);
    }

    private void showContainer(){
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
    }
    //
    //
    //database async task

    class LogoutAsyncTask extends AsyncTask<Void, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(Void... params) {
            return serverAPIManager.getAuthServiceAPI().logOut();
        }
        protected void onPostExecute(HttpResponse httpResponse) {
            handleLogoutResult(httpResponse);
        }
    }
}