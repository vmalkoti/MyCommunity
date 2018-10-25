package com.malkoti.capstone.mycommunity.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.Management;


// https://developer.android.com/topic/libraries/architecture/viewmodel#java

/**
 *
 */
public class AppLoginViewModel extends ViewModel {
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> userPassword = new MutableLiveData<>();

    private final MutableLiveData<AppUser> manager = new MutableLiveData<>();
    private final MutableLiveData<Community> managedCommunity = new MutableLiveData<>();
    private final MutableLiveData<Management> management = new MutableLiveData<>();


    public MutableLiveData<String> getUserEmail() {
        return userEmail;
    }

    public MutableLiveData<String> getUserPassword() {
        return userPassword;
    }

    public MutableLiveData<AppUser> getManager() {
        return manager;
    }

    public MutableLiveData<Community> getManagedCommunity() {
        return managedCommunity;
    }

    public MutableLiveData<Management> getManagement() {
        return management;
    }

    public void setUserEmail(String email) {
        userEmail.setValue(email);
    }

    public void setUserPassword(String password) {
        userPassword.setValue(password);
    }

    public void setManager(AppUser user) {
        manager.setValue(user);
    }

    public void setManagedCommunity(Community community) {
        managedCommunity.setValue(community);
    }

    public void setManagement(Management mgmt) {
        management.setValue(mgmt);
    }
}
