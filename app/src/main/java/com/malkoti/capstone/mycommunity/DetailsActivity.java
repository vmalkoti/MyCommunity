package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
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
import com.malkoti.capstone.mycommunity.model.AnnouncementPost;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity
        implements EditApartmentDetails.OnFragmentInteractionListener,
        EditCommunityDetails.OnFragmentInteractionListener,
        EditResidentDetails.OnFragmentInteractionListener,
        EditMaintenanceDetails.OnFragmentInteractionListener,
        EditAnnouncementDetails.OnFragmentInteractionListener {

    private static final String LOG_TAG = "DEBUG_" + DetailsActivity.class.getSimpleName();
    private static final boolean EDIT_MODE_ENABLED = false; // editing is disabled in current version

    public enum DetailsScreenType {COMMUNITY_DETAILS, APARTMENT_DETAILS, RESIDENT_DETAILS, MAINTENANCE_REQ_DETAILS, ANNOUNCEMENT_DETAILS}

    public static final String DISPLAY_SCREEN_TYPE = "DETAILS_TYPE";
    public static final String DISPLAY_ITEM_KEY = "DETAILS_ITEM_KEY";
    public static final String DISPLAY_ITEM_DETAILS = "DISPLAY_ITEM_DETAILS";

    private ActivityDetailsBinding binding;

    private DetailsScreenType type;
    private String itemKey;

    private DetailsViewModel viewModel;


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

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.getSignedInUser();

        // item = getIntent().getParcelableExtra(DISPLAY_ITEM_DETAILS);

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
                updateMaintenanceReqInfo(itemKey);
                break;
            case ANNOUNCEMENT_DETAILS:
                updateAnnouncementInfo(itemKey);
                break;
            default:
                Log.d(LOG_TAG, "Unknown fragment interaction");
        }
    }

    /**
     * Show fragment to display information
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

        if(EDIT_MODE_ENABLED) {
            binding.fabEditDetails.show();
        }
    }


    /**
     * Show fragment to edit information
     * @param type
     */
    private void showItemEditableFragment(DetailsScreenType type, String key) {
        Fragment fragment;

        switch (type) {
            case COMMUNITY_DETAILS:
                if (key==null || key.trim().equals("")) {
                    viewModel.setSelectedCommunity(Community.getDummyObject());
                }
                fragment = EditCommunityDetails.newInstance();
                break;
            case APARTMENT_DETAILS:
                if (key==null || key.trim().equals("")) {
                    viewModel.setSelectedApartment(Apartment.getDummyObject());
                }
                fragment = EditApartmentDetails.newInstance();
                break;
            case RESIDENT_DETAILS:
                if (key==null || key.trim().equals("")) {
                    viewModel.setSelectedUser(AppUser.getDummyObject());
                }
                fragment = EditResidentDetails.newInstance();
                break;
            case MAINTENANCE_REQ_DETAILS:
                if (key==null || key.trim().equals("")) {
                    viewModel.setSelectedRequest(MaintenanceRequest.getDummyObject());
                }
                fragment = EditMaintenanceDetails.newInstance(true);
                break;
            case ANNOUNCEMENT_DETAILS:
                if (key==null || key.trim().equals("")) {
                    viewModel.setSelectedAnnouncement(AnnouncementPost.getDummyObject());
                }
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
     * Update information received from Community screen
     * @param id
     */
    public void updateCommunityInfo(String id) {
        // update/create entry in "communities"
        if(id==null || id.trim().equals("")) {
            viewModel.createNewCommunity();
        }
        finish();
    }

    /**
     *
     * @param id
     */
    public void updateApartmentInfo(String id) {
        // update/create entry in "apartments"
        if(id==null || id.trim().equals("")) {
            viewModel.createNewApartment();
        }
        finish();
    }

    /**
     *
     * @param id
     */
    public void updateResidentInfo(String id) {
        // update/create entry in "users"
        if(id==null || id.trim().equals("")) {
            viewModel.createNewResident();
        }
        finish();
    }

    /**
     *
     * @param id
     */
    public void updateAnnouncementInfo(String id) {
        // update/create entry in "announcements"
        if(id==null || id.trim().equals("")) {
            viewModel.createNewAnnouncement();
        }
        finish();
    }

    /**
 *
     * @param id
     */
    public void updateMaintenanceReqInfo(String id) {
        // update/create entry in "requests"
        if(id==null || id.trim().equals("")) {
            viewModel.createNewRequest();
        }
        finish();
    }

}
