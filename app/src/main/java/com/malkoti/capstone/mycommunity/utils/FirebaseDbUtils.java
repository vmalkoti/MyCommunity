package com.malkoti.capstone.mycommunity.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malkoti.capstone.mycommunity.model.AnnouncementPost;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
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
    public static final String APTS_NODE_NAME = "apartments";
    public static final String APTS_NODE_PATH = "/apartments/";
    public static final String REQUESTS_NODE_NAME = "requests";
    public static final String REQUESTS_NODE_PATH = "/requests/";
    public static final String ANNOUNCEMENTS_NODE_NAME = "announcements";
    public static final String ANNOUNCEMENTS_NODE_PATH = "/announcements/";

    private FirebaseDbUtils() {
        throw new RuntimeException("Cannot instantiate this class");
    }


    /**
     * Create new User node for manager.
     * Also creates community and management nodes.
     * @param uid
     * @param manager
     * @param community
     * @param management
     */
    public static void createNewManagerInDb(@NonNull String uid,
                                            @NonNull AppUser manager,
                                            @NonNull Community community,
                                            @NonNull Management management) {
        Log.d(LOG_TAG, "createNewManagerInDb: For uid " + uid);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootNode = database.getReference();
        String cid = rootNode.child(FirebaseDbUtils.COMMUNITY_NODE_NAME).push().getKey();
        String mid = rootNode.child(FirebaseDbUtils.MGMT_NODE_NAME).push().getKey();

        // set cross-references : manager user
        manager.communityId = cid;
        manager.aptId = "";
        manager.mgmtId = mid;
        // set cross-references : community
        community.mgrId = uid;
        community.mgmtId = mid;
        // set cross-references : management
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
                Log.e(LOG_TAG, "createNewManagerInDb: Error creating manager and related nodes.",
                        databaseError.toException());
            }
        });
    }


    /**
     * Create new apartment node
     * @param apartment
     */
    public static void createApartment(@NonNull Apartment apartment) {
        Log.d(LOG_TAG, "createApartment: For apartment " + apartment.aptName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersNode = database.getReference(APTS_NODE_NAME);

        usersNode.setValue(apartment).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "createApartment: Added apartment node to the db: " + apartment.aptName);
            } else {
                Log.e(LOG_TAG, "createApartment: Error adding apartment to the db: " + apartment.aptName,
                        task.getException());
            }
        });
    }

    /**
     * Create new User node for resident
     * @param user
     */
    public static void createResident(@NonNull AppUser user) {
        Log.d(LOG_TAG, "createResident: For user " + user.fullName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersNode = database.getReference(USERS_NODE_NAME);

        usersNode.setValue(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "createResident: Added resident node to the db: " + user.fullName);
            } else {
                Log.e(LOG_TAG, "createResident: Error adding resident to the db: " + user.fullName,
                        task.getException());
            }
        });
    }

    /**
     * Create new Maintenance Request node
     * @param request
     */
    public static void createMaintenanceRequest(@NonNull MaintenanceRequest request) {
        Log.d(LOG_TAG, "createMaintenanceRequest: For request " + request.reqType);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference requestNode = database.getReference(REQUESTS_NODE_NAME);

        requestNode.setValue(request).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "createMaintenanceRequest: Added request node to the db: " + request.reqType);
            } else {
                Log.e(LOG_TAG, "createMaintenanceRequest: Error adding request to the db: " + request.reqType,
                        task.getException());
            }
        });
    }

    /**
     * Creates new Announcement/Notice post
     * @param post
     */
    public static void createAnnouncementPost(@NonNull AnnouncementPost post) {
        Log.d(LOG_TAG, "createAnnouncementPost: For post " + post.announcementTitle);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference announcementsNode = database.getReference(ANNOUNCEMENTS_NODE_NAME);

        announcementsNode.setValue(post).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d(LOG_TAG, "createAnnouncementPost: Added post node to the db: " + post.announcementTitle);
            } else {
                Log.e(LOG_TAG, "createAnnouncementPost: Error adding post to the db: " + post.announcementTitle,
                        task.getException());
            }
        });
    }




}
