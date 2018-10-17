package com.malkoti.capstone.mycommunity;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Preferences screen for the app
 */
public class AppPreferences extends PreferenceFragmentCompat {

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
    }


    /**
     *
     */
    private void setUpPrefListeners() {
        Preference aboutButton = getPreferenceManager().findPreference("about_pref");
        if (aboutButton != null) {
            aboutButton.setOnPreferenceClickListener(preference -> {
                showAlert(getString(R.string.about_app_msg));
                return true;
            });
        }

        Preference logoutButton = getPreferenceManager().findPreference("logout_pref");
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

