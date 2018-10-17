package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentUserAuthBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserAuthentication.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserAuthentication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserAuthentication extends Fragment {
    private static final String LOG_TAG = UserAuthentication.class.getSimpleName();
    //private FirebaseAuth firebaseAuth;
    FragmentUserAuthBinding loginBinding;

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
        void onUserLogin(String emailId, String password, boolean isExistingUser);
        void onUserSignUp(boolean forNewResident);
    }


    public UserAuthentication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static UserAuthentication newInstance() {
        UserAuthentication fragment = new UserAuthentication();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // loginBinding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_user_auth);
        //firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_user_auth, container, false);

        setUpButtonListeners();

        return loginBinding.getRoot();

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
    private void setUpButtonListeners() {
        loginBinding.authenticateUser.setOnClickListener(view -> onLoginButtonClick());
        loginBinding.signupUserResident.setOnClickListener(view -> onSignUpNewResidentButtonClick());
        loginBinding.signupUserMgmt.setOnClickListener(view -> onSignUpNewMgmtButtonClick());
    }


    /**
     * Authenticate user credentials from Firebase
     */
    private void onLoginButtonClick() {
        final String userId = loginBinding.userLoginId.getText().toString();
        final String userPassword = loginBinding.userLoginPassword.getText().toString();

        interactionListener.onUserLogin(userId, userPassword, true);
    }


    public void onSignUpNewResidentButtonClick() {
        interactionListener.onUserSignUp(true);
    }


    /**
     * Create new user account in Firebase
     */
    public void onSignUpNewMgmtButtonClick() {
        interactionListener.onUserSignUp(false);
    }


}
