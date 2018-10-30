package com.malkoti.capstone.mycommunity.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
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
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;

public class DetailsViewModel extends ViewModel {
    private static final String LOG_TAG = "DEBUG_" + DetailsViewModel.class.getSimpleName();

    private final MutableLiveData<AppUser> signedInUser = new MutableLiveData<>();
    private final MutableLiveData<String> signedInUserId = new MutableLiveData<>();

    private final MutableLiveData<AppUser> selectedUser = new MutableLiveData<>();
    private final MutableLiveData<Apartment> selectedApartment = new MutableLiveData<>();
    private final MutableLiveData<Community> selectedCommunity = new MutableLiveData<>();
    private final MutableLiveData<MaintenanceRequest> selectedRequest = new MutableLiveData<>();
    private final MutableLiveData<AnnouncementPost> selectedAnnouncement = new MutableLiveData<>();

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

    /**
     * Get details of currently signed in user
     * @return
     */
    public MutableLiveData<AppUser> getSignedInUser() {
        Log.d(LOG_TAG, "getSignedInUser: get signed in user's node object for reference later");

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // for managers
        database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                .child(FirebaseAuthUtil.getSignedInUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        signedInUserId.setValue(dataSnapshot.getKey());
                        signedInUser.setValue(dataSnapshot.getValue(AppUser.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(LOG_TAG, "createNewApartment: Error getting user object",
                                databaseError.toException());
                    }
                });

        return signedInUser;
    }

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

    /*
     * For future implementation. Right now, community can only be created on sign-up
     */
    public void createNewCommunity() {
        Log.d(LOG_TAG, "createNewCommunity: For community " + selectedCommunity.getValue().name);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference communitiesNode = database.getReference(FirebaseDbUtils.COMMUNITY_NODE_NAME);

        communitiesNode.push().setValue(selectedUser.getValue());
    }

    /**
     *
     */
    public void createNewApartment() {
        Log.d(LOG_TAG, "createNewApartment: For apartment " + selectedApartment.getValue().aptName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        /*
        database.getReference(FirebaseDbUtils.USERS_NODE_NAME)
                .child(FirebaseAuthUtil.getSignedInUserId())
                .child("communityId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String communityId = dataSnapshot.getValue(String.class);
                        selectedApartment.getValue().communityId = communityId;

                        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
                        aptsNode.push().setValue(selectedApartment.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(LOG_TAG, "createNewApartment: Error getting community ID of the manager",
                                databaseError.toException());
                    }
                });
        */

        String communityId = signedInUser.getValue().communityId;
        selectedApartment.getValue().communityId = communityId;

        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);
        aptsNode.push().setValue(selectedApartment.getValue());


    }

    /**
     *
     */
    public void createNewResident() {
        Log.d(LOG_TAG, "createNewResident: For resident " + selectedUser.getValue().fullName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME);

        usersNode.push().setValue(selectedUser.getValue());
    }

    /**
     *
     */
    public void createNewAnnouncement() {
        Log.d(LOG_TAG, "createNewAnnouncement: For announcement " + selectedAnnouncement.getValue().announcementTitle);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);
        selectedAnnouncement.getValue().managerId = signedInUserId.getValue();
        selectedAnnouncement.getValue().communityId = signedInUser.getValue().communityId;
        announcementsNode.push().setValue(selectedAnnouncement.getValue());
    }

    /**
     *
     */
    public void createNewRequest() {
        Log.d(LOG_TAG, "createNewRequest: For user " + selectedRequest.getValue().reqType);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);

        requestsNode.push().setValue(selectedUser.getValue());
    }


    /**
     * @param key
     * @return
     */
    private void setSelectedResident(String key) {
        Log.d(LOG_TAG, "setSelectedResident: For user " + key);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME).child(key);
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "getResident: Value retrieved");
                selectedUser.setValue(dataSnapshot.getValue(AppUser.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(LOG_TAG, "getResident: Error getting value.", databaseError.toException());
            }
        });
    }
}
