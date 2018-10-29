package com.malkoti.capstone.mycommunity.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
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
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;

public class DetailsViewModel extends ViewModel {
    private static final String LOG_TAG = "DEBUG_" + DetailsViewModel.class.getSimpleName();

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

        communitiesNode.setValue(selectedUser.getValue());
    }

    public void createNewApartment() {
        Log.d(LOG_TAG, "createNewApartment: For apartment " + selectedApartment.getValue().aptName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference aptsNode = database.getReference(FirebaseDbUtils.APTS_NODE_NAME);

        aptsNode.setValue(selectedApartment.getValue());
    }

    public void createNewResident() {
        Log.d(LOG_TAG, "createNewResident: For resident " + selectedUser.getValue().fullName);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersNode = database.getReference(FirebaseDbUtils.USERS_NODE_NAME);

        usersNode.setValue(selectedUser.getValue());
    }

    public void createNewAnnouncement() {
        Log.d(LOG_TAG, "createNewAnnouncement: For announcement " + selectedAnnouncement.getValue().announcementTitle);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference announcementsNode = database.getReference(FirebaseDbUtils.ANNOUNCEMENTS_NODE_NAME);

        announcementsNode.setValue(selectedUser.getValue());
    }

    public void createNewRequest() {
        Log.d(LOG_TAG, "createNewRequest: For user " + selectedRequest.getValue().reqType);

        // https://stackoverflow.com/questions/39109616/should-firebasedatabase-getinstance-be-used-sparingly/39109665#39109665
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference requestsNode = database.getReference(FirebaseDbUtils.REQUESTS_NODE_NAME);

        requestsNode.setValue(selectedUser.getValue());
    }



    /**
     *
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
