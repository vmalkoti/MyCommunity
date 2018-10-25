package com.malkoti.capstone.mycommunity;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.crashlytics.android.Crashlytics;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;

/**
 * Preferences screen for the app
 */
public class AppPreferences extends PreferenceFragmentCompat {
    private OnFragmentInteractionListener interactionListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public AppPreferences() {
        // Required empty constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

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
                interactionListener.onFragmentInteraction(null);
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

