package com.malkoti.capstone.mycommunity.utils;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.malkoti.capstone.mycommunity.LoginActivity;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.Management;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDbUtils {
    private static final String LOG_TAG = "DEBUG_" + FirebaseDbUtils.class.getSimpleName();

    public static final String USERS_NODE_NAME = "users";
    public static final String USERS_NODE_PATH = "/users/";
    public static final String MGMT_NODE_NAME = "managements";
    public static final String MGMT_NODE_PATH = "/managements/";
    public static final String COMMUNITY_NODE_NAME = "communities";
    public static final String COMMUNITY_NODE_PATH = "/communities/";
    public static final String APTS_NODE_NAME = "apartements";
    public static final String APTS_NODE_PATH = "/apartements/";
    public static final String REQUESTS_NODE_NAME = "requests";
    public static final String REQUESTS_NODE_PATH = "/requests/";
    public static final String ANNOUNCEMENTS_NODE_NAME = "announcements";
    public static final String ANNOUNCEMENTS_NODE_PATH = "/announcements/";

    private FirebaseDbUtils() {}


    public void fetchDbUser(String uid) {

    }

    public void fetchUserCommunities(String uid) {

    }

    public void fetchCommunityApartments(String communityId) {

    }

    public void fetchUserApartment(String uid) {

    }

    public void fetchCommunityResidents(String communityId) {

    }

    public void fetchResidentProfile(String uid) {

    }

    public void fetchCommunityMaintenanceRequests(String communityId) {

    }

    public void fetchUserMaintenanceRequests(String uid) {

    }

    public void fetchCommunityNotices(String communityId) {

    }


    /**
     *
     */
    public static void createNewManagerInDb(String uid, AppUser manager, Community community, Management management) {
        //String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(LOG_TAG, "createNewManagerInDb: For uid " + uid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootNode = database.getReference();
        String cid = rootNode.child(FirebaseDbUtils.COMMUNITY_NODE_NAME).push().getKey();
        String mid = rootNode.child(FirebaseDbUtils.MGMT_NODE_NAME).push().getKey();

        // set cross-references
        manager.communityId = cid;
        manager.aptId = "";
        community.mgrId = uid;
        community.mgmtId = mid;
        management.mgrId = uid;

        // Fan-out approach for updates
        // All or nothing
        Map<String, Object> childUpdates = new HashMap<>();
        // create node in "users" with uid
        childUpdates.put(FirebaseDbUtils.USERS_NODE_PATH + uid, manager.toMap());
        // create node in "communities"
        childUpdates.put(FirebaseDbUtils.COMMUNITY_NODE_PATH + cid, community.toMap());
        // create node in "management"
        childUpdates.put(FirebaseDbUtils.MGMT_NODE_PATH + mid, management.toMap());

        // push update values to the database
        rootNode.updateChildren(childUpdates, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                Log.e(LOG_TAG, "createNewManagerInDb: \nERROR_MESSAGE - " + databaseError.getMessage()
                        + "\nERROR_DETAILS - " + databaseError.getDetails());
                //Toast.makeText(LoginActivity.this,
                //        "Error! " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
