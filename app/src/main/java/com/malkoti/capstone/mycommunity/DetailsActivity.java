package com.malkoti.capstone.mycommunity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.malkoti.capstone.mycommunity.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity
        implements EditApartmentDetails.OnFragmentInteractionListener,
        EditCommunityDetails.OnFragmentInteractionListener,
        EditResidentDetails.OnFragmentInteractionListener,
        EditMaintenanceDetails.OnFragmentInteractionListener,
        EditAnnouncementDetails.OnFragmentInteractionListener {

    private static final String LOG_TAG = "DEBUG_" + DetailsActivity.class.getSimpleName();

    public enum DetailsScreenType {COMMUNITY_DETAILS, APARTMENT_DETAILS, RESIDENT_DETAILS, MAINTENANCE_REQ_DETAILS, ANNOUNCEMENT_DETAILS}

    public static final String DISPLAY_SCREEN_TYPE = "DETAILS_TYPE";
    public static final String DISPLAY_ITEM_KEY = "DETAILS_ITEM_KEY";
    private ActivityDetailsBinding binding;

    private DetailsScreenType type;
    private String itemKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_details);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        // show action bar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Get inputs to the fragment
        Intent activityStartIntent = getIntent();
        type = (DetailsScreenType) activityStartIntent.getSerializableExtra(DISPLAY_SCREEN_TYPE);
        itemKey = activityStartIntent.getStringExtra(DISPLAY_ITEM_KEY);

        // attach onclick listener to floating action button
        binding.fabEditDetails.setOnClickListener(v -> {
            Log.d("DetailsActivity_LOG", "Showing edit screen");
            showItemEditableFragment(type, itemKey);
        });


        // if activity/fragment is being recreated, do nothing
        if (savedInstanceState != null) return;

        // else create and show appropriate fragment
        Log.d("DetailsActivity_LOG", "key received: " + itemKey);
        // check if a key was passed
        if (itemKey != null) {
            Log.d("DetailsActivity_LOG", "Showing info screen");
            showItemInfoFragment(type);
        } else {
            Log.d("DetailsActivity_LOG", "Showing edit screen");
            showItemEditableFragment(type, null);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            binding.fabEditDetails.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        switch (type) {
            case COMMUNITY_DETAILS:
                updateCommunityInfo(itemKey);
                break;
            case APARTMENT_DETAILS:
                updateApartmentInfo(itemKey);
                break;
            case RESIDENT_DETAILS:
                updateResidentInfo(itemKey);
                break;
            case MAINTENANCE_REQ_DETAILS:
                break;
            case ANNOUNCEMENT_DETAILS:
                break;
                default:
                    Log.d(LOG_TAG, "Unknown fragment interaction");
        }
    }

    /**
     * @param type
     */
    private void showItemInfoFragment(DetailsScreenType type) {
        Fragment fragment;

        switch (type) {
            case COMMUNITY_DETAILS:
                fragment = ViewCommunityInfo.newInstance();
                break;
            case APARTMENT_DETAILS:
                fragment = ViewApartmentInfo.newInstance();
                break;
            case RESIDENT_DETAILS:
                fragment = ViewResidentInfo.newInstance();
                break;
            case MAINTENANCE_REQ_DETAILS:
                fragment = ViewMaintenanceReqInfo.newInstance();
                break;
            case ANNOUNCEMENT_DETAILS:
                fragment = ViewAnnouncementInfo.newInstance();
                break;
            default:
                return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        binding.fabEditDetails.show();
    }


    /**
     * @param type
     */
    private void showItemEditableFragment(DetailsScreenType type, String key) {
        Fragment fragment;

        switch (type) {
            case COMMUNITY_DETAILS:
                fragment = EditCommunityDetails.newInstance();
                break;
            case APARTMENT_DETAILS:
                fragment = EditApartmentDetails.newInstance();
                break;
            case RESIDENT_DETAILS:
                fragment = EditResidentDetails.newInstance();
                break;
            case MAINTENANCE_REQ_DETAILS:
                fragment = EditMaintenanceDetails.newInstance(true);
                break;
            case ANNOUNCEMENT_DETAILS:
                fragment = EditAnnouncementDetails.newInstance();
                break;
            default:
                return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (key != null) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

        binding.fabEditDetails.hide();
    }

    /**
     *
     * @param id
     */
    public void updateCommunityInfo(String id) {
        // update/create entry in "communities"

        // if new, add entry under management's "communities" node

    }

    /**
     *
     * @param id
     */
    public void updateApartmentInfo(String id) {
        // update/create entry in "apartments"

        // if new, add entry under community's "apartments" node

    }

    /**
     *
     * @param id
     */
    public void updateResidentInfo(String id) {
        // update/create entry in "users"

        // if new, add entry under community's "residents" node

    }

    /**
     *
     * @param id
     */
    public void updateAnnouncementInfo(String id) {
        // update/create entry in "announcements"


    }

    /**
 *
     * @param id
     */
    public void updateMaintenanceReqInfo(String id) {
        // update/create entry in "requests"

        // if new, add entry under user's "requests" node
    }

}
