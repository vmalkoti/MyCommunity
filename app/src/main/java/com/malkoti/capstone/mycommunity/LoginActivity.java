package com.malkoti.capstone.mycommunity;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.malkoti.capstone.mycommunity.databinding.ActivityLoginBinding;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.Management;
import com.malkoti.capstone.mycommunity.utils.FirebaseAuthUtil;
import com.malkoti.capstone.mycommunity.utils.FirebaseDbUtils;
import com.malkoti.capstone.mycommunity.viewmodels.AppLoginViewModel;


/**
 *
 */
public class LoginActivity extends AppCompatActivity
        implements UserAuthentication.OnFragmentInteractionListener,
        UserSignup.OnFragmentInteractionListener {
    private static final String LOG_TAG = "DEBUG_" + LoginActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;

    private ActivityLoginBinding binding;
    private AppLoginViewModel loginViewModel;

    public static final int RESULT_SUCCESS = 1001;
    public static final int RESULT_FAILURE = 1002;
    public static final int RESULT_CANCELLED = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate: Showing LoginActivity");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.loginProgressbar.hide();

        loginViewModel = ViewModelProviders.of(this).get(AppLoginViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.login_container, UserAuthentication.newInstance())
                    .commit();
        }
    }

    @Override
    public void onUserLogin(String emailId, String password) {

        binding.loginProgressbar.show();

        FirebaseAuthUtil.authenticateUser(emailId, password, new FirebaseAuthUtil.ICallback() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, "authenticateUser:success");
                binding.loginProgressbar.hide();
                setResult(AppCompatActivity.RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(@Nullable String failureMessage) {
                // If sign in fails, display a message to the user.
                Log.w(LOG_TAG, "authenticateUser: Failure - " + failureMessage);
                binding.loginProgressbar.hide();
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //authenticateUser(emailId, password);

    }


    @Override
    public void onUserSignUp(boolean forNewResident) {
        UserSignup.SignupType signupType;
        Log.d(LOG_TAG, "Signin up new user. Is a Resident? " + forNewResident);

        if (forNewResident) {
            signupType = UserSignup.SignupType.FOR_RESIDENT;
        } else {
            signupType = UserSignup.SignupType.FOR_MANAGER;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_container, UserSignup.newInstance(signupType))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onSignUpScreenSubmitAction() {
        Log.d(LOG_TAG, "onSignUpScreenSubmitAction: started");

        binding.loginProgressbar.show();

        String email = loginViewModel.getUserEmail().getValue();
        String password = loginViewModel.getUserPassword().getValue();

        // NOTE: Use RxJava to improve this callback code spaghetti

        // Create new user account
        FirebaseAuthUtil.signUpNewUser(email, password, new FirebaseAuthUtil.ICallback() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, "Signup new user success");

                /*
                FirebaseAuthUtil.authenticateUser(email, password, new FirebaseAuthUtil.ICallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(LOG_TAG, "authenticateUser:success");

                        binding.loginProgressbar.hide();
                        setResult(AppCompatActivity.RESULT_OK);

                        // Create new management and building nodes
                        createNewManagerInDb(FirebaseAuthUtil.getSignedInUserId());

                        finish();
                    }

                    @Override
                    public void onFailure(@Nullable String failureMessage) {
                        // If sign in fails, display a message to the user.
                        Log.w(LOG_TAG, "authenticateUser: Failure - " + failureMessage);
                        binding.loginProgressbar.hide();
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                */

                binding.loginProgressbar.hide();
                setResult(AppCompatActivity.RESULT_OK);
                Log.d(LOG_TAG, "onSignUpScreenSubmitAction: In signup success, uid is " +
                    FirebaseAuth.getInstance().getCurrentUser().getUid());

                // Create new management and building nodes
                createNewManagerInDb(FirebaseAuthUtil.getSignedInUserId());

                Log.d(LOG_TAG, "Closing Login Activity");

                finish();

            }

            @Override
            public void onFailure(@Nullable String failureMessage) {
                Log.d(LOG_TAG, "authenticateUser: Failure - " + failureMessage);
                binding.loginProgressbar.hide();
                Toast.makeText(LoginActivity.this,
                        "Error creating new user account: " + failureMessage, Toast.LENGTH_LONG).show();
            }
        });


        setResult(RESULT_SUCCESS);

        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(AppCompatActivity.RESULT_CANCELED);
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
        if(firebaseAuth.getCurrentUser() == null) {
            setResult(AppCompatActivity.RESULT_CANCELED);
        } else {
            setResult(AppCompatActivity.RESULT_OK);
        }
        */
    }


    /**
     * @param message
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setMessage(message)
                .setCancelable(true)
                .setNeutralButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     *
     */
    public void createNewManagerInDb(String uid) {
        FirebaseDbUtils.createNewManagerInDb(uid,
                loginViewModel.getManager().getValue(),
                loginViewModel.getManagedCommunity().getValue(),
                loginViewModel.getManagement().getValue());
    }
}
