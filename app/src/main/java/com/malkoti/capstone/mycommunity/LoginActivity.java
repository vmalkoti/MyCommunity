package com.malkoti.capstone.mycommunity;


import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.malkoti.capstone.mycommunity.databinding.ActivityLoginBinding;


/**
 *
 */
public class LoginActivity extends AppCompatActivity
        implements UserAuthentication.OnFragmentInteractionListener,
        UserSignup.OnFragmentInteractionListener {
    private static final String LOG_TAG = "DEBUG_" + LoginActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;

    ActivityLoginBinding binding;

    public static final int RESULT_SUCCESS = 1001;
    public static final int RESULT_FAILURE = 1002;
    public static final int RESULT_CANCELLED = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate: Showing LoginActivity");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.loginProgressbar.hide();

        firebaseAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_container, UserAuthentication.newInstance())
                .commit();
    }

    @Override
    public void onUserLogin(String emailId, String password, boolean isExistingUser) {
        Log.d(LOG_TAG, "isExistingUser=" + isExistingUser);

        binding.loginProgressbar.show();

        if (isExistingUser) {
            authenticateUser(emailId, password);
        } else  {
            signUpNewUser(emailId, password);
        }
    }


    @Override
    public void onUserSignUp(boolean forNewResident) {
        Log.d(LOG_TAG, "Signin up new user. Is a Resident? " + forNewResident);

        if(forNewResident) {
            // show an alert tht functionality is not yet available
            AlertDialog.Builder builder;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setMessage(getString(R.string.resident_signup_alert))
                    .setCancelable(true)
                    .setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            // show sign-up screen for management
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_container, UserSignup.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public void onSignUpScreenSubmitAction() {
        Toast.makeText(LoginActivity.this, "New user signed up.",
                Toast.LENGTH_SHORT).show();

        Log.d(LOG_TAG, "onSignUpScreenSubmitAction: executing");

        // TODO: Create new user account; read from activity owned livedata

        // TODO: Create new user, management and building nodes

        setResult(RESULT_SUCCESS);

        finish();
    }

    /**
     *
     * @param userId
     * @param userPassword
     */
    private void authenticateUser(String userId, String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(userId, userPassword)
                .addOnCompleteListener(LoginActivity.this,
                        task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(LOG_TAG, "authenticateUser:success");
                                binding.loginProgressbar.hide();
                                setResult(RESULT_SUCCESS);
                                // login/signup was successful, show MainActivity
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(LOG_TAG, "authenticateUser:failure", task.getException());
                                binding.loginProgressbar.hide();
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    /**
     *
     * @param userId
     * @param userPassword
     */
    private void signUpNewUser(String userId, String userPassword) {
        firebaseAuth.createUserWithEmailAndPassword(userId, userPassword)
                .addOnCompleteListener(LoginActivity.this,
                        task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(LOG_TAG, "signUpNewUser:success");
                                binding.loginProgressbar.hide();
                                setResult(RESULT_SUCCESS);
                                finish();

                            }
                            /*
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(UserAuthentication.this.getContext(),
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            */
                        })
                .addOnFailureListener(LoginActivity.this,
                        ex -> {
                            Log.e(LOG_TAG, "signUpNewUser:failure", ex.getCause());
                            binding.loginProgressbar.hide();
                            Toast.makeText(LoginActivity.this,
                                    ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(this, "User already signed in", Toast.LENGTH_SHORT).show();
            // show MainActivity
            finish();
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELLED);
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
        if(firebaseAuth.getCurrentUser() == null) {
            setResult(RESULT_CANCELLED);
        } else {
            setResult(RESULT_SUCCESS);
        }
        */
    }
}
