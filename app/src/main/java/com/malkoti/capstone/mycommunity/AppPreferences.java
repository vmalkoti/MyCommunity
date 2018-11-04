package com.malkoti.capstone.mycommunity;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.crashlytics.android.Crashlytics;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

/**
 * Preferences screen for the app
 */
public class AppPreferences extends PreferenceFragmentCompat {
    private static final String LOG_TAG = "DEBUG_" + AppPreferences.class.getSimpleName();

    private OnFragmentInteractionListener interactionListener;
    private MainViewModel viewModel;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Parcelable selectedItem);
    }


    public AppPreferences() {
        // Required empty constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        setUpPrefListeners();
    }

    public static AppPreferences newInstance() {
        AppPreferences fragment = new AppPreferences();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            /*
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
            */
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }


    /**
     *
     */
    private void setUpPrefListeners() {
        Preference aboutButton = getPreferenceManager().findPreference("about_pref");
        Preference profileButton = getPreferenceManager().findPreference("profile_pref");
        //Preference passwordChange = getPreferenceManager().findPreference("passwd_pref");
        Preference simulateCrash = getPreferenceManager().findPreference("crash_pref");
        Preference logoutButton = getPreferenceManager().findPreference("logout_pref");

        if (aboutButton != null) {
            aboutButton.setOnPreferenceClickListener(preference -> {
                showAlert(getString(R.string.about_app_msg));
                return true;
            });
        }

        if(profileButton != null) {
            profileButton.setOnPreferenceClickListener(preference -> {
                AppUser currentUser = viewModel.getSignedInUser().getValue();
                if(currentUser.isManager) {
                    Community community = viewModel.getCommunity().getValue();
                    interactionListener.onFragmentInteraction(community);
                } else {
                    interactionListener.onFragmentInteraction(currentUser);
                }
                return true;
            });
        }

        /*
        if(passwordChange != null) {
            passwordChange.setOnPreferenceChangeListener((preference, object) -> {
                showAlert("Changing password to " + object);
                return true;
            });
        }
        */

        if(simulateCrash != null) {
            simulateCrash.setOnPreferenceClickListener(preference -> {
                Crashlytics.getInstance().crash();
                return true;
            });
        }

        if (logoutButton != null) {
            logoutButton.setOnPreferenceClickListener(preference -> {
                //showAlert("Clicked on Logout button");
                FirebaseAuthUtil.signOut();
                return true;
            });
        }
    }


    /**
     *
     * @param message
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("My Community App")
                .setMessage(message)
                .setCancelable(true)
                .setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }
}

