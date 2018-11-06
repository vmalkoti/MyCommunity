package com.malkoti.capstone.mycommunity.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import android.content.Context;
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
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Viewmodel class for MainActivity
 */
public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "DEBUG_" + MainViewModel.class.getSimpleName();

    private final MutableLiveData<Community> community = new MutableLiveData<>();
    private final MutableLiveData<List<AppUser>> residents = new MutableLiveData<>();
    private final MutableLiveData<List<Apartment>> apartments = new MutableLiveData<>();
    private final MutableLiveData<List<MaintenanceRequest>> requests = new MutableLiveData<>();
    private final MutableLiveData<List<AnnouncementPost>> announcements = new MutableLiveData<>();

    private final MutableLiveData<AppUser> signedInUser = new MutableLiveData<>();
    private final MutableLiveData<String> signedInUserId = new MutableLiveData<>();

    private ChildEventListener communityListener;
    private ChildEventListener residentListener;
    private ChildEventListener aptListener;
    private ChildEventListener requestListener;
    private ChildEventListener announcementListener;

    public MainViewModel(@NonNull Application application) {
        super(application);
        setSignedInUser();
        initializeLists();
        initializeListeners();
    }

    public MutableLiveData<Community> getCommunity() {
        return community;
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
     * initialize mutable data with blank lists
     */
    private void initializeLists() {
        residents.setValue(new ArrayList<>());
        apartments.setValue(new ArrayList<>());
        requests.setValue(new ArrayList<>());
        announcements.setValue(new ArrayList<>());
    }

    /**
     * Set livedata with details of currently signed in user from firebase
     *
     * @return
     */
    private void setSignedInUser() {
        Log.d(LOG_TAG, "setSignedInUser: get signed in user's node object for reference later");

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String userEmailId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.d(LOG_TAG, "setSignedInUser: Email id = " + userEmailId);

        database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                .orderByChild("email")
                .equalTo(userEmailId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d(LOG_TAG, "setSignedInUser: User info recieved for " + dataSnapshot.getKey());

                        signedInUserId.setValue(dataSnapshot.getKey());
                        signedInUser.setValue(dataSnapshot.getValue(AppUser.class));
                        signedInUser.getValue().userKey = dataSnapshot.getKey();

                        if (signedInUser.getValue().isManager) {
                            initializeManagerViewLists();
                        } else {
                            initializeResidentViewLists();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(LOG_TAG, "setSignedInUser: Error getting user object",
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

        String communityId = signedInUser.getValue().communityId;
        // database.getReference(FirebaseDbUtils.USERS_NODE_NAME).child(uid).child("communityId").toString();

        Log.d(LOG_TAG, "initializeManagerViewLists: Reading database to nodes for community " + communityId);

        DatabaseReference communityNode = database.getReference(FirebaseDbUtils.COMMUNITY_NODE_NAME);
        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
        DatabaseReference residentsNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME);
        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

        communityNode.child(communityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Community c = dataSnapshot.getValue(Community.class);
                community.setValue(c);
                Log.d(LOG_TAG, "initializeManagerViewLists: community " + c.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "initializeManagerViewLists: error getting community.", databaseError.toException());
            }
        });


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

        String userId = signedInUser.getValue().userKey;
        String communityId = signedInUser.getValue().communityId;
        String aptId = signedInUser.getValue().aptId;

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference communityNode = database.getReference(FirebaseDbUtils.COMMUNITY_NODE_NAME);
        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
        DatabaseReference residentsNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME);
        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

        communityNode.child(communityId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Community c = dataSnapshot.getValue(Community.class);
                community.setValue(c);
                Log.d(LOG_TAG, "initializeResidentViewLists: community " + c.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "initializeResidentViewLists: error getting community.", databaseError.toException());
            }
        });


        // get only those nodes that are related to the resident
        Query aptsQuery = aptsNode.child(aptId);
        aptsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Apartment newApt = dataSnapshot.getValue(Apartment.class);
                newApt.aptKey = dataSnapshot.getKey();
                apartments.setValue(Arrays.asList(newApt));
                Log.d(LOG_TAG, "aptValueEventListener: apt added " + newApt.aptName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LOG_TAG, "aptValueEventListener: error!",  databaseError.toException());
            }
        });

        Query residentsQuery = residentsNode.orderByChild("aptId").equalTo(aptId);
        residentsQuery.addChildEventListener(residentListener);

        Query requestsQuery = requestsNode.orderByChild("residentId").equalTo(userId);
        requestsQuery.addChildEventListener(requestListener);
        Query announcementsQuery = announcementsNode.orderByChild("communityId").equalTo(communityId);
        announcementsQuery.addChildEventListener(announcementListener);

    }

    /**
     * Initialize childeventlisteners for firebase node queries
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
                // to fire observers
                apartments.setValue(apartments.getValue());
                Log.d(LOG_TAG, "aptListener: apt added " + newApt.aptName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Apartment removedApt = dataSnapshot.getValue(Apartment.class);
                apartments.getValue().remove(removedApt);
                // to fire observers
                apartments.setValue(apartments.getValue());
                Log.d(LOG_TAG, "aptListener: apt removed " + removedApt.aptName);
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
                if (!newResident.isManager) {
                    newResident.userKey = dataSnapshot.getKey();
                    //getApartmentById(newResident.aptId).observe(this, apt -> newResident.aptName = apt.aptName);
                    residents.getValue().add(newResident);
                    // to fire observers
                    residents.setValue(residents.getValue());
                    Log.d(LOG_TAG, "residentListener: resident added " + newResident.fullName);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                AppUser removedResident = dataSnapshot.getValue(AppUser.class);
                residents.getValue().remove(removedResident);
                // to fire observers
                residents.setValue(residents.getValue());
                Log.d(LOG_TAG, "requestListener: request removed " + removedResident.fullName);
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
                // to fire observers
                requests.setValue(requests.getValue());

                Log.d(LOG_TAG, "requestListener: request added " + newRequest.reqType);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                MaintenanceRequest removedRequest = dataSnapshot.getValue(MaintenanceRequest.class);
                requests.getValue().remove(removedRequest);
                // to fire observers
                requests.setValue(requests.getValue());

                Log.d(LOG_TAG, "requestListener: request removed " + removedRequest.reqType);
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
                // to fire observers
                announcements.setValue(announcements.getValue());
                Log.d(LOG_TAG, "announcementListener: announcement added " + newAnnouncement.announcementTitle);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                AnnouncementPost removedAnnouncement = dataSnapshot.getValue(AnnouncementPost.class);
                announcements.getValue().remove(removedAnnouncement);
                // to fire observers
                announcements.setValue(announcements.getValue());
                Log.d(LOG_TAG, "announcementListener: announcement removed " + removedAnnouncement.announcementTitle);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
    }


    /**
     * Retrieve user from database using id
     * @param firebaseKey Firebase push id key value for user node
     * @return LiveData object
     */
    public MutableLiveData<AppUser> getResidentById(String firebaseKey) {
        MutableLiveData<AppUser> user = new MutableLiveData<>();

        Log.d(LOG_TAG, "getResidentById: For resident " + firebaseKey);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME).child(firebaseKey);
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "getResidentById: Value retrieved");
                user.setValue(dataSnapshot.getValue(AppUser.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "getResidentById: Error getting value.", databaseError.toException());
            }
        });

        return user;
    }


    /**
     * Retrieve apartment from database using id
     *  @param firebaseKey Firebase push id key value for apartment node
     *  @return LiveData object
     */
    public MutableLiveData<Apartment> getApartmentById(String firebaseKey) {
        MutableLiveData<Apartment> apartment = new MutableLiveData<>();

        Log.d(LOG_TAG, "getApartmentById: For apartment " + firebaseKey);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference aptNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME).child(firebaseKey);
        aptNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "getApartmentById: Value retrieved");
                apartment.setValue(dataSnapshot.getValue(Apartment.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "getApartmentById: Error getting value.", databaseError.toException());
            }
        });

        return apartment;
    }

}
