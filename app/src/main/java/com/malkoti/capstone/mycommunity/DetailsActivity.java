package com.malkoti.capstone.mycommunity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.malkoti.capstone.mycommunity.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
    public enum DetailsScreenType {COMMUNITY_DETAILS, APARTMENT_DETAILS, RESIDENT_DETAILS, MAINTENANCE_REQ_DETAILS, ANNOUNCEMENT_DETAILS}

    public static final String DISPLAY_SCREEN_TYPE = "DETAILS_TYPE";
    public static final String DISPLAY_ITEM_KEY = "DETAILS_ITEM_KEY";
    ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_details);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        // show action bar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // if activity is being recreated, do nothing
        if(savedInstanceState != null) return;

        // show fragment
        Intent activityStartIntent = getIntent();
        DetailsScreenType type = (DetailsScreenType) activityStartIntent.getSerializableExtra(DISPLAY_SCREEN_TYPE);
        String itemKey = activityStartIntent.getStringExtra(DISPLAY_ITEM_KEY);

        Log.d("DetailsActivity_LOG", "key received: " + itemKey);
        // check if a key was passed
        if(itemKey != null) {
            Log.d("DetailsActivity_LOG", "Showing info screen");
            showItemInfoFragment(type);
        } else  {
            Log.d("DetailsActivity_LOG", "Showing edit screen");
            showItemEditableFragment(type, null);
        }

        binding.fabEditDetails.setOnClickListener(v -> {
            Log.d("DetailsActivity_LOG", "Showing edit screen");
            showItemEditableFragment(type, itemKey);
        });

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

    /**
     *
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
     *
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
        if(key!=null) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

        binding.fabEditDetails.hide();
    }
}
