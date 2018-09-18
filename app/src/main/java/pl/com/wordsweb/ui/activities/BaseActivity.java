package pl.com.wordsweb.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import pl.com.wordsweb.R;
import pl.com.wordsweb.event.OnBackStackEvent;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.fragments.AddNewCommentFragment;
import pl.com.wordsweb.ui.fragments.AnswersFragment;
import pl.com.wordsweb.ui.fragments.CommentsFragment;
import pl.com.wordsweb.ui.fragments.LearnViewPagerFragment;
import pl.com.wordsweb.ui.fragments.LessonListFragment;
import pl.com.wordsweb.ui.fragments.MainPageFragment;
import pl.com.wordsweb.ui.fragments.MyLessonListFragment;
import pl.com.wordsweb.ui.fragments.MyQuizzesListFragment;

import static pl.com.wordsweb.config.AppSettings.bus;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG_COMMENT = "tag_comment";
    public static String TAG_COMMENT_ADD = "tag_comment_add";
    public Toolbar toolbar;
    protected SharedPreferencesProvider sharedPreferencesProvider;
    protected FloatingActionButton fab;
    protected ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResId, activityContainer, true);
        super.setContentView(drawerLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        setUpNavView();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!useFabButton()) {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().findFragmentByTag(TAG_COMMENT_ADD) instanceof AddNewCommentFragment) {
                getSupportFragmentManager().popBackStack();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else if (getSupportFragmentManager().findFragmentByTag(TAG_COMMENT_ADD) instanceof AnswersFragment) {
                getSupportFragmentManager().popBackStack();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else if (getSupportFragmentManager().findFragmentByTag(TAG_COMMENT_ADD) instanceof AddNewCommentFragment) {
                getSupportFragmentManager().popBackStack();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else if (getSupportFragmentManager().findFragmentByTag(TAG_COMMENT) instanceof CommentsFragment) {
                getSupportFragmentManager().popBackStack();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                enableNavi();
            } else {
                bus.post(new OnBackStackEvent());
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    enableNavi();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_main_page:
                openFragment(new MainPageFragment());
                setTitle(getResources().getString(R.string.title_module_main));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_manage_page:
                openFragment(new LessonListFragment());
                setTitle(getResources().getString(R.string.title_module_list));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_my_manage_page:
                openFragment(new MyLessonListFragment());
                setTitle(getResources().getString(R.string.title_module_my_list));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_learn_page:
                openFragment(new LearnViewPagerFragment());
                setTitle(getResources().getString(R.string.title_module_learn));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_my_quizz:
                openFragment(new MyQuizzesListFragment());
                setTitle(getResources().getString(R.string.title_module_quizzes));
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_logout:
                logout();
                return true;
            default:
                openFragment(new MainPageFragment());
                drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    public void enableNavi() {
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disableNavi() {
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void enableBackArrow() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    protected boolean useFabButton() {
        return true;
    }

    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void openFragment(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void openFragmentWithBack(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openFragmentWithBack(Fragment fragment, Bundle args, String TAG) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    public String getAppBarTitle() {
        return toolbar.getTitle().toString();
    }

    public void hideFab() {
        fab.setVisibility(View.GONE);
    }

    public void showFab() {
        fab.setVisibility(View.VISIBLE);
    }

    public void logout() {
        sharedPreferencesProvider.removeToken();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
