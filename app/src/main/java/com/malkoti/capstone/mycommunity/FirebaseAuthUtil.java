package com.malkoti.capstone.mycommunity;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 *
 */
public class FirebaseAuthUtil {
    private static final String LOG_TAG = "DEBUG_" + FirebaseAuthUtil.class.getSimpleName();

    /**
     * Callback interface for Firebase Auth events
     */
    public interface ICallback {
        void onSuccess();
        void onFailure(@Nullable String failureMessage);
    }

    /**
     * Authenticate user with provided credentials
     *
     * @param emailId
     * @param password
     * @param callback
     */
    public static void authenticateUser(@NonNull String emailId, @NonNull String password,
                                        @NonNull ICallback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(LOG_TAG, "authenticateUser:success");
                        callback.onSuccess();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(LOG_TAG, "authenticateUser:failure", task.getException());
                        callback.onFailure(task.getException().getLocalizedMessage());
                    }
                });
    }

    /**
     * Create new user account with provided credentials
     *
     * @param emailId
     * @param password
     * @param callback
     */
    public static void signUpNewUser(@NonNull String emailId, @NonNull String password,
                                     @NonNull ICallback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(LOG_TAG, "signUpNewUser:success");
                        callback.onSuccess();

                    } /* else {
                                // If sign in fails, display a message to the user.
                                Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(UserAuthentication.this.getContext(),
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                    } */
                })
                .addOnFailureListener(ex -> {
                            Log.e(LOG_TAG, "signUpNewUser:failure", ex.getCause());
                            callback.onFailure(ex.getLocalizedMessage());
                        });
    }


    /**
     *
     * @return
     */
    public static boolean isUserSignedIn() {
        return (FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     *
     * @param emailId
     * @param currentPassword
     * @param newPassword
     * @param callback
     */
    public static void changeUserPassword(@NonNull String emailId, @NonNull String currentPassword,
                                          @NonNull String newPassword,
                                          @NonNull ICallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(emailId, currentPassword);
        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> setNewPassword(user, newPassword, callback))
                .addOnFailureListener(ex -> callback.onFailure(ex.getLocalizedMessage()));
    }

    private static void setNewPassword(FirebaseUser user, String newPassword, ICallback callback) {
        user.updatePassword(newPassword)
                .addOnSuccessListener(task -> callback.onSuccess())
                .addOnFailureListener(ex -> callback.onFailure(ex.getLocalizedMessage()));
    }
}
