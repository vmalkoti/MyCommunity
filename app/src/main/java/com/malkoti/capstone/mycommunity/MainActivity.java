package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.malkoti.capstone.mycommunity.databinding.ActivityMainBinding;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

public class MainActivity
        extends AppCompatActivity
        implements ViewApartmentsList.OnFragmentInteractionListener,
        ViewResidentsList.OnFragmentInteractionListener,
        ViewMaintenanceList.OnFragmentInteractionListener,
        ViewAnnouncementsList.OnFragmentInteractionListener,
        AppPreferences.OnFragmentInteractionListener {
    private static final String LOG_TAG = "DEBUG_" + MainActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 2001;
    private static final int RC_DETAILS = 2002;
    private Intent signInLaunchIntent;

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    private AHBottomNavigationAdapter bottomNavAdapter;
    private BottomBarFragmentStatePagerAdapter pagerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (FirebaseAuthUtil.isUserSignedIn()) {
                Log.d(LOG_TAG, "AuthListener: User logged in");
                if(viewModel.getSignedInUser().getValue()==null) {
                    viewModel.initSignedInUserData();
                }
                initUI();
                initAds();
                //attachDatabaseEventListeners();
            } else {
                Log.d(LOG_TAG, "AuthListener: User not logged in");
                //removeDatabaseEventListeners();
                viewModel.setSignedInUser(null);
                startActivityForResult(signInLaunchIntent, RC_SIGN_IN, null);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "onCreate: Started");
        signInLaunchIntent = new Intent(MainActivity.this, LoginActivity.class);

        firebaseAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //if(savedInstanceState != null) return;

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        if (FirebaseAuthUtil.isUserSignedIn()) {
            Log.d(LOG_TAG, "onCreate: Use Signed in");

            initUI();
            initAds();
            //attachDatabaseEventListeners();
        } else {
            Log.d(LOG_TAG, "onCreate: Not signed in");
            //startActivityForResult(signInLaunchIntent, RC_SIGN_IN, null);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause: removing auth state listener");
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume: adding auth state listener");
        firebaseAuth.addAuthStateListener(authStateListener);

        // need to fix issue - show correct fragment in viewpage after rotation
        // https://stackoverflow.com/questions/27619610/android-viewpager-with-tab-not-maintaining-state-after-screen-rotation

        if (FirebaseAuthUtil.isUserSignedIn()) {
            // set the visibility of FAB - required to show/hide after rotatio
            Log.d(LOG_TAG, "onResume: Currently selected bottom nav item: "
                    + binding.bottomNavigation.getCurrentItem());
            int position = binding.bottomNavigation.getCurrentItem();
            binding.viewpager.setCurrentItem(position, false);
            setFabVisibility(binding.bottomNavigation.getCurrentItem());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d(LOG_TAG, "onActivityResult: Sign in screen result received " + resultCode);
            if (resultCode == RESULT_CANCELED) {
                Log.d(LOG_TAG, "onActivityResult: Cancelled");
                finish();
            } else {
                Log.d(LOG_TAG, "onActivityResult: Sign in successful");
                Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Initialize UI components
     */
    private void initUI() {
        //viewModel.initSignedInUserData();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        setupViewPager();
        setupBottomNavBar();

        // show bottom navigation bar
        showAppNavigation(true);

        binding.bottomFab.setOnClickListener(v -> {
            int shownFragment = binding.bottomNavigation.getCurrentItem();
            DetailsActivity.DetailsScreenType screenType;

            switch (shownFragment) {
                case 0:
                    screenType = DetailsActivity.DetailsScreenType.APARTMENT_DETAILS;
                    break;
                case 1:
                    screenType = DetailsActivity.DetailsScreenType.RESIDENT_DETAILS;
                    break;
                case 2:
                    screenType = DetailsActivity.DetailsScreenType.MAINTENANCE_REQ_DETAILS;
                    break;
                case 3:
                    screenType = DetailsActivity.DetailsScreenType.ANNOUNCEMENT_DETAILS;
                    break;
                case 4:
                    // settings screen
                    return;
                default:
                    return;
            }
            // creating new item; so no key to pass
            showDetailsScreenForItem(screenType, null, null);
        });
    }

    /**
     * Initialize Admob and load ads
     */
    private void initAds() {
        MobileAds.initialize(this, getString(R.string.admob_sample_app_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adview.loadAd(adRequest);
    }

    /**
     * Set the ViewPager.
     * Add fragments and associate with adapter
     */
    private void setupViewPager() {
        binding.viewpager.setPagingEnabled(false);
        pagerAdapter = new BottomBarFragmentStatePagerAdapter(getSupportFragmentManager());

        // make sure that fragments are in same order as menu items
        pagerAdapter.addFragment(ViewApartmentsList.newInstance());
        pagerAdapter.addFragment(ViewResidentsList.newInstance());
        pagerAdapter.addFragment(ViewMaintenanceList.newInstance());
        pagerAdapter.addFragment(ViewAnnouncementsList.newInstance());
        pagerAdapter.addFragment(AppPreferences.newInstance());

        binding.viewpager.setAdapter(pagerAdapter);
    }

    /**
     * Set up components for the BottomNavigation in the activity
     */
    private void setupBottomNavBar() {
        Log.d(LOG_TAG, "setupBottomNavBar: Setting up bottom navigation");
        bottomNavAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navbar_menu);
        bottomNavAdapter.setupWithBottomNavigation(binding.bottomNavigation);

        binding.bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (!wasSelected) {
                Log.d(LOG_TAG, "Navbar button clicked =" + position);
                binding.viewpager.setCurrentItem(position, false);
            }
            setFabVisibility(position);

            return true;
        });

        // always show the text of each item on bottom navbar
        binding.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // for translucent android navbar, also enable item in theme styles.xml
        // if true, change height of push_toolbar_view
        // and change bottompadding of fab
        //binding.bottomNavigation.setTranslucentNavigationEnabled(true);

        // hide bottomNavBar on scroll?
        binding.bottomNavigation.setBehaviorTranslationEnabled(true);
        // show first item of the tab
        //binding.bottomNavigation.setCurrentItem(0);
        setFabVisibility(binding.bottomNavigation.getCurrentItem());
    }


    /**
     * @param bottomNavSelectedItemPosition
     */
    private void setFabVisibility(int bottomNavSelectedItemPosition) {
        if(bottomNavAdapter == null) {
            Log.d(LOG_TAG, "setupBottomNavBar: bottom nav bar adapter is null");
            return;
        }

        String title = binding.bottomNavigation.getItem(bottomNavSelectedItemPosition).getTitle(this);
        Log.d(LOG_TAG, "setupBottomNavBar: Setting up FAB visibility for item " + title);

        if (viewModel.getSignedInUser().getValue() == null) {
            Log.d(LOG_TAG, "setupBottomNavBar: No signedinuse in viewmodel");
            return;
        }

        boolean isManager = viewModel.getSignedInUser().getValue().isManager;
        if (isManager) {
            if(bottomNavSelectedItemPosition == 2 || bottomNavSelectedItemPosition == 4) {
                // manager - hide on maintenance request and settings screen
                binding.bottomFab.hide();
            } else {
                binding.bottomFab.show();
            }
        } else {
            if(bottomNavSelectedItemPosition == 2) {
                // resident - show only on maintenance request screen
                binding.bottomFab.show();
            } else {
                binding.bottomFab.hide();
            }
        }
    }

    /**
     * Toggle visibility of BottomNavigationBar and Toolbar
     *
     * @param visible
     */
    private void showAppNavigation(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        binding.bottomNavigation.setVisibility(visibility);
        binding.toolbar.setVisibility(visibility);
    }


    /**
     * Create a notification badge on BottomNavigationBar item
     *
     * @param position   BottomNavigationBar item position
     * @param badgeCount Number to display as badge
     */
    private void createBottomBarNotification(int position, String badgeCount) {
        Handler handler = new Handler();

        handler.postDelayed(() -> {
            AHNotification notification = new AHNotification.Builder()
                    .setText(badgeCount)
                    .setBackgroundColor(getResourceColor(R.color.colorAccent))
                    .setTextColor(getResourceColor(R.color.colorPrimaryDark))
                    .build();
            binding.bottomNavigation.setNotification(notification, position);
        }, 1000);


    }

    /**
     * Return Color for a given color resource id
     *
     * @param colorResource ID of R.color resource
     * @return
     */
    private int getResourceColor(int colorResource) {
        return ContextCompat.getColor(this, colorResource);
    }

    @Override
    public void onFragmentInteraction(Parcelable selectedItem) {
        Log.d(LOG_TAG, "onFragmentInteraction: showing details for parcelable");

        int position = binding.bottomNavigation.getCurrentItem();

        switch (position) {
            case 0:
                showDetailsScreenForItem(DetailsActivity.DetailsScreenType.APARTMENT_DETAILS,
                        "apt1234", selectedItem);
                break;
            case 1:
                showDetailsScreenForItem(DetailsActivity.DetailsScreenType.RESIDENT_DETAILS,
                        "res1234", selectedItem);
                break;
            case 2:
                showDetailsScreenForItem(DetailsActivity.DetailsScreenType.MAINTENANCE_REQ_DETAILS,
                        "req1234", selectedItem);
                break;
            case 3:
                showDetailsScreenForItem(DetailsActivity.DetailsScreenType.ANNOUNCEMENT_DETAILS,
                        "ads1234", selectedItem);
                break;
            case 4:
                if (viewModel.getSignedInUser().getValue().isManager) {
                    // if manager, show community details screen
                    showDetailsScreenForItem(DetailsActivity.DetailsScreenType.COMMUNITY_DETAILS,
                            "bldg1234", selectedItem);
                } else {
                    // if resident, show resident details screen
                    showDetailsScreenForItem(DetailsActivity.DetailsScreenType.RESIDENT_DETAILS,
                            "res1234", selectedItem);
                }
            default:
                Log.e(LOG_TAG, "Unknown fragment listener");
        }
    }


    /**
     * @param screenType
     * @param key
     */
    private void showDetailsScreenForItem(DetailsActivity.DetailsScreenType screenType,
                                          String key, Parcelable selectedItemObject) {
        Log.d(LOG_TAG, "showDetailsScreenForItem: showing details for dummy key=" + key);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.DISPLAY_SCREEN_TYPE, screenType);
        intent.putExtra(DetailsActivity.DISPLAY_ITEM_KEY, key);
        intent.putExtra(DetailsActivity.DISPLAY_ITEM_DETAILS, selectedItemObject);
        //intent.putExtra(DetailsActivity.DISPLAY_ITEM_DETAILS, (Parcelable) null);

        startActivity(intent);
    }

}
