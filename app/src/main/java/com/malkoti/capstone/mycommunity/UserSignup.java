package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentUserSignupBinding;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.model.Management;
import com.malkoti.capstone.mycommunity.viewmodels.AppLoginViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserSignup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserSignup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSignup extends Fragment {
    public enum SignupType {FOR_RESIDENT, FOR_MANAGER}
    private static final String BUNDLE_ARG_KEY = "SIGNUP_TYPE";
    private SignupType type;

    private static final String LOG_TAG = "DEBUG_" + UserSignup.class.getSimpleName();

    private OnFragmentInteractionListener interactionListener;

    private FragmentUserSignupBinding binding;

    private AppLoginViewModel loginViewModel;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onSignUpScreenSubmitAction();
    }

    public UserSignup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserSignup.
     */
    public static UserSignup newInstance(SignupType type) {
        UserSignup fragment = new UserSignup();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_ARG_KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = (SignupType) getArguments().getSerializable(BUNDLE_ARG_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginViewModel = ViewModelProviders.of(getActivity()).get(AppLoginViewModel.class);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_signup,
                container, false);
        binding.signupUser.setOnClickListener(view -> verifyAndSignupNewUser());


        if(savedInstanceState != null) {
            type = (SignupType) getArguments().getSerializable(BUNDLE_ARG_KEY);
        }

        initUI();

        //binding.signUpEntryFields.setCommunity(community);

        Log.d(LOG_TAG, "Sign up type = " + type);

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialize fragment UI
     */
    private void initUI() {
        // show or hide community info fields depending on type
        if(type == SignupType.FOR_RESIDENT) {
            binding.communityInfoHeaderTv.setVisibility(View.GONE);
            binding.signUpEntryFields.communityInfoContainer.setVisibility(View.GONE);
            binding.userLoginEmailHint.setVisibility(View.VISIBLE);
        } else {
            binding.communityInfoHeaderTv.setVisibility(View.VISIBLE);
            binding.signUpEntryFields.communityInfoContainer.setVisibility(View.VISIBLE);
            binding.userLoginEmailHint.setVisibility(View.GONE);
        }
    }

    /**
     * Check if all fields contain valid values and call fragment listener
     */
    private void verifyAndSignupNewUser() {
        boolean isVerified = verifyUserCredentialFields();
        Log.d(LOG_TAG, "verifyAndSignupNewUser: credential fields verified? " + isVerified);

        if (type == SignupType.FOR_MANAGER) {
            isVerified = isVerified && verifyCommunityFields();
            Log.d(LOG_TAG, "verifyAndSignupNewUser: community fields verified? " + isVerified);
        }

        Log.d(LOG_TAG, "verifyAndSignupNewUser: user verified? " + isVerified);

        if(isVerified) {
            // call fragment listener
            interactionListener.onSignUpScreenSubmitAction();
        }
    }

    /**
     * Verify values in new Community creation fields
     *
     * @return False if one or more fields have invalid values, else true
     */
    private boolean verifyCommunityFields() {
        // TODO: Read fields values and check
        final String name = binding.signUpEntryFields.communityNameEt.getText().toString().trim();
        final String description = binding.signUpEntryFields.communityDescEt.getText().toString().trim();
        final String streetAddress = binding.signUpEntryFields.communityStreetAddressEt.getText().toString().trim();
        final String city = binding.signUpEntryFields.communityCityEt.getText().toString().trim();
        final String state = binding.signUpEntryFields.communityStateEt.getText().toString().trim();
        final String zip = binding.signUpEntryFields.communityZipEt.getText().toString().trim();
        final String country = binding.signUpEntryFields.communityCountryEt.getText().toString().trim();
        final String phone = binding.signUpEntryFields.communityPhoneEt.getText().toString().trim();

        if (name.equals("")) {
            binding.signUpEntryFields.communityNameEt.setError("Need a community name");
            return false;
        }
        if (streetAddress.equals("")) {
            binding.signUpEntryFields.communityStreetAddressEt.setError("Address is required");
            return false;
        }
        if (city.equals("")) {
            binding.signUpEntryFields.communityCityEt.setError("City is required");
            return false;
        }
        if (state.equals("")) {
            binding.signUpEntryFields.communityStateEt.setError("State is required");
            return false;
        }
        if (zip.equals("")) {
            binding.signUpEntryFields.communityZipEt.setError("Zip is required");
            return false;
        }
        if (phone.equals("")) {
            binding.signUpEntryFields.communityPhoneEt.setError("Phone number is required");
            return false;
        }

        // update viewmodel

        loginViewModel.setManagedCommunity(new Community(name, description, streetAddress, city, state, zip, country, phone));
        loginViewModel.setManagement(new Management(name));

        //loginViewModel.setManagedCommunity(binding.signUpEntryFields.getCommunity());
        //loginViewModel.setManagement(new Management(binding.signUpEntryFields.getCommunity().name));

        loginViewModel.setManager(new AppUser(name, true,
                "", binding.userLoginIdEt.getText().toString().trim(), phone, "", "", ""));

        return true;
    }

    /**
     * Verify values in new user account creation fields
     *
     * @return False if one or more fields have invalid values, else true
     */
    private boolean verifyUserCredentialFields() {
        final String emailId = binding.userLoginIdEt.getText().toString().trim();
        final String password = binding.userLoginPasswordEt.getText().toString().trim();
        final String confirmPassword = binding.userConfirmPasswordEt.getText().toString().trim();

        if (emailId.equals("")) {
            binding.userLoginIdEt.setError("An email ID is needed");
            return false;
        }
        if (password.equals("")) {
            binding.userLoginPasswordEt.setError("Enter a password");
            return false;
        }
        if (confirmPassword.equals("") || !confirmPassword.equals(password)) {
            binding.userConfirmPasswordEt.setError("Passwords do not match");
            return false;
        }

        // update viewmodel
        loginViewModel.setUserEmail(emailId.trim());
        loginViewModel.setUserPassword(password.trim());

        return true;
    }

}
