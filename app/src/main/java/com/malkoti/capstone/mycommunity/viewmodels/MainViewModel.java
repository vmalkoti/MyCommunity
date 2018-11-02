package com.malkoti.capstone.mycommunity.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.malkoti.capstone.mycommunity.model.AnnouncementPost;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;

import java.util.List;

/**
 *
 */
public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "DEBUG_" + MainViewModel.class.getSimpleName();

    private final MutableLiveData<List<AppUser>> residents = new MutableLiveData<>();
    private final MutableLiveData<List<Apartment>> apartments = new MutableLiveData<>();
    private final MutableLiveData<List<MaintenanceRequest>> requests = new MutableLiveData<>();
    private final MutableLiveData<List<AnnouncementPost>> announcements = new MutableLiveData<>();

    private final MutableLiveData<AppUser> signedInUser = new MutableLiveData<>();
    private final MutableLiveData<String> signedInUserId = new MutableLiveData<>();

    private ChildEventListener residentListener;
    private ChildEventListener aptListener;
    private ChildEventListener requestListener;
    private ChildEventListener announcementListener;

    public MainViewModel(@NonNull Application application) {
        super(application);
        setSignedInUser();
        initializeListeners();
    }

    public MutableLiveData<List<AppUser>> getResidents() {
        return residents;
    }

    public MutableLiveData<List<Apartment>> getApartments() {
        return apartments;
    }

    public MutableLiveData<List<MaintenanceRequest>> getRequests() {
        return requests;
    }

    public MutableLiveData<List<AnnouncementPost>> getAnnouncements() {
        return announcements;
    }

    public MutableLiveData<AppUser> getSignedInUser() {
        return signedInUser;
    }

    /**
     * Set livedata with details of currently signed in user from firebase
     *
     * @return
     */
    private void setSignedInUser() {
        Log.d(LOG_TAG, "getSignedInUser: get signed in user's node object for reference later");

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String userEmailId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                .orderByChild("email")
                .equalTo(userEmailId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        signedInUserId.setValue(dataSnapshot.getKey());
                        signedInUser.setValue(dataSnapshot.getValue(AppUser.class));
                        signedInUser.getValue().userKey = dataSnapshot.getKey();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(LOG_TAG, "createNewApartment: Error getting user object",
                                databaseError.toException());
                        Toast.makeText(getApplication().getApplicationContext(),
                                "Error getting signed in user details: " + databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Initialize LiveData lists required for Manager view
     */
    public void initializeManagerViewLists() {
        Log.d(LOG_TAG, "initializeManagerViewLists: Reading database to get lists for manager");

        String uid = FirebaseAuthUtil.getSignedInUserId();

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String mgmtId = signedInUser.getValue().mgmtId;
        String communityId = signedInUser.getValue().communityId;
        // database.getReference(FirebaseDbUtils.USERS_NODE_NAME).child(uid).child("communityId").toString();

        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
        DatabaseReference residentsNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME);
        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

        Query aptsQuery = aptsNode.orderByChild("communityId").equalTo(communityId);
        aptsQuery.addChildEventListener(aptListener);
        Query residentsQuery = residentsNode.orderByChild("communityId").equalTo(communityId);
        residentsQuery.addChildEventListener(residentListener);
        Query requestsQuery = requestsNode.orderByChild("communityId").equalTo(communityId);
        requestsQuery.addChildEventListener(requestListener);
        Query announcementsQuery = announcementsNode.orderByChild("communityId").equalTo(communityId);
        announcementsQuery.addChildEventListener(announcementListener);
    }

    /**
     * Initialize LiveData lists required for Resident view
     */
    public void initializeResidentViewLists() {
        Log.d(LOG_TAG, "initializeResidentViewLists: Reading database to get lists for resident");

        String userEmailId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String mgmtId = signedInUser.getValue().mgmtId;
        String aptId = signedInUser.getValue().aptId;
        String communityId = signedInUser.getValue().communityId;

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

        Query requestsQuery = requestsNode.orderByChild("residentId").equalTo(signedInUserId.getValue());
        requestsQuery.addChildEventListener(requestListener);
        Query announcementsQuery = announcementsNode.orderByChild("communityId").equalTo(communityId);
        announcementsQuery.addChildEventListener(announcementListener);

    }

    /**
     *
     */
    private void initializeListeners() {
        // Listener for apartment node
        Log.d(LOG_TAG, "initializeListeners: Initializing apt listener");
        aptListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Apartment newApt = dataSnapshot.getValue(Apartment.class);
                newApt.aptKey = dataSnapshot.getKey();
                apartments.getValue().add(newApt);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Apartment removedApt = dataSnapshot.getValue(Apartment.class);
                apartments.getValue().remove(removedApt);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        // Listener for users node - residents
        Log.d(LOG_TAG, "initializeListeners: Initializing resident listener");
        residentListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AppUser newResident = dataSnapshot.getValue(AppUser.class);
                newResident.userKey = dataSnapshot.getKey();
                residents.getValue().add(newResident);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                AppUser removedResident = dataSnapshot.getValue(AppUser.class);
                residents.getValue().remove(removedResident);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        // Listener for maintenance requests node
        Log.d(LOG_TAG, "initializeListeners: Initializing requests listener");
        requestListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MaintenanceRequest newRequest = dataSnapshot.getValue(MaintenanceRequest.class);
                newRequest.reqKey = dataSnapshot.getKey();
                requests.getValue().add(newRequest);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                MaintenanceRequest removedRequest = dataSnapshot.getValue(MaintenanceRequest.class);
                requests.getValue().remove(removedRequest);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        // Listener for announcements node
        Log.d(LOG_TAG, "initializeListeners: Initializing announcements listener");
        announcementListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AnnouncementPost newAnnouncement = dataSnapshot.getValue(AnnouncementPost.class);
                newAnnouncement.postKey = dataSnapshot.getKey();
                announcements.getValue().add(newAnnouncement);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                AnnouncementPost removedAnnouncement = dataSnapshot.getValue(AnnouncementPost.class);
                announcements.getValue().remove(removedAnnouncement);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
    }
}
