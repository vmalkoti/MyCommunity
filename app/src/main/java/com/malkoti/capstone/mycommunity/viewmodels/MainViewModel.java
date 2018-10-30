package com.malkoti.capstone.mycommunity.viewmodels;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.malkoti.capstone.mycommunity.model.AnnouncementPost;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;

import java.util.List;

public class MainViewModel extends ViewModel {
    private static final String LOG_TAG = "DEBUG_" + MainViewModel.class.getSimpleName();

    private final MutableLiveData<List<AppUser>> residents = new MutableLiveData<>();
    private final MutableLiveData<List<Apartment>> apartments = new MutableLiveData<>();
    private final MutableLiveData<List<MaintenanceRequest>> requests = new MutableLiveData<>();
    private final MutableLiveData<List<AnnouncementPost>> announcements = new MutableLiveData<>();

    private ChildEventListener residentListener;
    private ChildEventListener aptListener;
    private ChildEventListener requestListener;
    private ChildEventListener announcementListener;

    public MainViewModel() {
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

    /**
     *
     */
    public void initializeManagerViewLists() {
        Log.d(LOG_TAG, "initializeManagerViewLists: Reading database to get lists for manager");

        String uid = FirebaseAuthUtil.getSignedInUserId();

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String communityId = database.getReference(FirebaseDbUtils.USERS_NODE_NAME).child(uid).child("communityId").toString();

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
     *
     */
    public void initializeResidentViewLists() {
        Log.d(LOG_TAG, "initializeResidentViewLists: Reading database to get lists for manager");

        String userEmailId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Query residentQuery = database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                .orderByChild("email").equalTo(userEmailId);
        residentQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String residentId = dataSnapshot.getKey();
                String communityId = database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                        .child(residentId).child("communityId").toString();

                DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);
                DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

                Query requestsQuery = requestsNode.orderByChild("residentId").equalTo(residentId);
                requestsQuery.addChildEventListener(requestListener);
                Query announcementsQuery = announcementsNode.orderByChild("communityId").equalTo(communityId);
                announcementsQuery.addChildEventListener(announcementListener);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


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
        residentListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MaintenanceRequest newRequest = dataSnapshot.getValue(MaintenanceRequest.class);
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

        // Listener for maintenance requests node
        Log.d(LOG_TAG, "initializeListeners: Initializing announcements listener");
        residentListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AnnouncementPost newAnnouncement = dataSnapshot.getValue(AnnouncementPost.class);
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
