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
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.model.Management;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class DetailsViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "DEBUG_" + DetailsViewModel.class.getSimpleName();

    private final MutableLiveData<AppUser> signedInUser = new MutableLiveData<>();
    private final MutableLiveData<String> signedInUserId = new MutableLiveData<>();

    private final MutableLiveData<AppUser> selectedUser = new MutableLiveData<>();
    private final MutableLiveData<Apartment> selectedApartment = new MutableLiveData<>();
    private final MutableLiveData<Community> selectedCommunity = new MutableLiveData<>();
    private final MutableLiveData<MaintenanceRequest> selectedRequest = new MutableLiveData<>();
    private final MutableLiveData<AnnouncementPost> selectedAnnouncement = new MutableLiveData<>();

    /*
     * Constructor matching super
     */
    public DetailsViewModel(@NonNull Application application) {
        super(application);
        setSignedInUser();
    }



    /* GETTERS */
    public MutableLiveData<AppUser> getSelectedUser() {
        return selectedUser;
    }

    public MutableLiveData<Apartment> getSelectedApartment() {
        return selectedApartment;
    }

    public MutableLiveData<Community> getSelectedCommunity() {
        return selectedCommunity;
    }

    public MutableLiveData<MaintenanceRequest> getSelectedRequest() {
        return selectedRequest;
    }

    public MutableLiveData<AnnouncementPost> getSelectedAnnouncement() {
        return selectedAnnouncement;
    }

    public MutableLiveData<AppUser> getSignedInUser() {
        return signedInUser;
    }

    /* SETTERS */
    public void setSelectedUser(AppUser user) {
        this.selectedUser.setValue(user);
    }

    public void setSelectedApartment(Apartment apartment) {
        this.selectedApartment.setValue(apartment);
    }

    public void setSelectedCommunity(Community community) {
        this.selectedCommunity.setValue(community);
    }

    public void setSelectedRequest(MaintenanceRequest request) {
        this.selectedRequest.setValue(request);
    }

    public void setSelectedAnnouncement(AnnouncementPost post) {
        this.selectedAnnouncement.setValue(post);
    }

    /**
     * Set livedata with details of currently signed in user from firebase
     *
     * @return
     */
    private void setSignedInUser() {
        Log.d(LOG_TAG, "initSignedInUserData: get signed in user's node object for reference later");

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String userEmailId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Log.d(LOG_TAG, "initSignedInUserData: Email id = " + userEmailId);

        database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                .orderByChild("email")
                .equalTo(userEmailId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.d(LOG_TAG, "initSignedInUserData: User info recieved for " + dataSnapshot.getKey());

                        signedInUserId.setValue(dataSnapshot.getKey());
                        signedInUser.setValue(dataSnapshot.getValue(AppUser.class));
                        signedInUser.getValue().userKey = dataSnapshot.getKey();

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
                        Log.e(LOG_TAG, "initSignedInUserData: Error getting user object",
                                databaseError.toException());
                        Toast.makeText(getApplication().getApplicationContext(),
                                "Error getting signed in user details: " + databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    /*
     * Create new node in community.
     * For future implementation. Right now, community can only be created on sign-up
     */
    public void createNewCommunity() {
        Log.d(LOG_TAG, "createNewCommunity: For community " + selectedCommunity.getValue().name);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference communitiesNode = database.getReference(FirebaseDbUtils.COMMUNITY_NODE_NAME);

        communitiesNode.push().setValue(selectedUser.getValue()).addOnCompleteListener(task -> {
            if(task.isCanceled()) {
                Toast.makeText(getApplication().getApplicationContext(),
                        "Error getting signed in user details: " + task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Create new node in apartments
     */
    public void createNewApartment() {
        Log.d(LOG_TAG, "createNewApartment: For apartment " + selectedApartment.getValue().aptName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String communityId = signedInUser.getValue().communityId;
        selectedApartment.getValue().communityId = communityId;

        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
        aptsNode.push().setValue(selectedApartment.getValue()).addOnCompleteListener(task -> {
            if(task.isCanceled()) {
                Toast.makeText(getApplication().getApplicationContext(),
                        "Error getting signed in user details: " + task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Create new node in users
     */
    public void createNewResident() {
        Log.d(LOG_TAG, "createNewResident: For resident " + selectedUser.getValue().fullName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME);

        usersNode.push().setValue(selectedUser.getValue()).addOnCompleteListener(task -> {
            if(task.isCanceled()) {
                Toast.makeText(getApplication().getApplicationContext(),
                        "Error getting signed in user details: " + task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Create new node in announcements
     */
    public void createNewAnnouncement() {
        Log.d(LOG_TAG, "createNewAnnouncement: For announcement " + selectedAnnouncement.getValue().announcementTitle);

        selectedAnnouncement.getValue().managerId = signedInUser.getValue().userKey;
        selectedAnnouncement.getValue().communityId = signedInUser.getValue().communityId;

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

        announcementsNode.push().setValue(selectedAnnouncement.getValue()).addOnCompleteListener(task -> {
            if(task.isCanceled()) {
                Toast.makeText(getApplication().getApplicationContext(),
                        "Error getting signed in user details: " + task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Create new node in maintenance request
     */
    public void createNewRequest() {
        Log.d(LOG_TAG, "createNewRequest: For request " + selectedRequest.getValue().reqType);

        selectedRequest.getValue().residentId = signedInUser.getValue().userKey;

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);

        requestsNode.push().setValue(selectedRequest.getValue()).addOnCompleteListener(task -> {
            if(task.isCanceled()) {
                Toast.makeText(getApplication().getApplicationContext(),
                        "Error getting signed in user details: " + task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
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

    /**
     * Retrieve management from database using id
     *  @param firebaseKey Firebase push id key value for management node
     *  @return LiveData object
     */
    public MutableLiveData<Management> getManagementById(String firebaseKey) {
        MutableLiveData<Management> mgmt = new MutableLiveData<>();

        Log.d(LOG_TAG, "getManagementById: For apartment " + firebaseKey);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mgmtNode = database.getReference(FirebaseDbUtils.MGMT_NODE_NAME).child(firebaseKey);
        mgmtNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "getManagementById: Value retrieved");
                mgmt.setValue(dataSnapshot.getValue(Management.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "getManagementById: Error getting value.", databaseError.toException());
            }
        });


        return  mgmt;
    }


    /**
     * Retrieve list of apartments using community id
     * @param communityId Firebase push id key value for community node
     * @return LiveData object
     */
    public MutableLiveData<List<Apartment>> getApartmentListOfCommunity(String communityId) {
        MutableLiveData<List<Apartment>> apartments = new MutableLiveData<>();
        apartments.setValue(new ArrayList<>());

        Log.d(LOG_TAG, "getApartmentListOfCommunity: Reading database to get apartment list");

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //String communityId = signedInUser.getValue().communityId;

        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
        Query aptsQuery = aptsNode.orderByChild("communityId").equalTo(communityId);
        aptsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Apartment newApt = dataSnapshot.getValue(Apartment.class);
                newApt.aptKey = dataSnapshot.getKey();
                apartments.getValue().add(newApt);
                // to fire observers
                apartments.setValue(apartments.getValue());
                Log.d(LOG_TAG, "getApartmentListOfCommunity : aptListener: apt added " + newApt.aptName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {  }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        return apartments;
    }
}
