package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentUserSignupBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserSignup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserSignup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSignup extends Fragment {
    private OnFragmentInteractionListener interactionListener;

    private FragmentUserSignupBinding binding;

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
    // TODO: Rename and change types and number of parameters
    public static UserSignup newInstance() {
        UserSignup fragment = new UserSignup();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_signup,
                container, false);

        binding.signupUser.setOnClickListener(v -> verifyAndSignupNewUser());
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

    private void verifyAndSignupNewUser() {
        verifyUserCredentialFields();
        verifyCommunityFields();

        // put all values in livedata objects with activity as owner

        interactionListener.onSignUpScreenSubmitAction();
    }

    /**
     *
     * @return
     */
    private boolean verifyCommunityFields() {
        // TODO: Read fields values and check

        return true;
    }

    /**
     *
     * @return
     */
    private boolean verifyUserCredentialFields() {
        final String emailId = binding.userLoginIdEt.getText().toString();
        final String password = binding.userLoginPasswordEt.getText().toString();
        final String confirmPassword = binding.userConfirmPasswordEt.getText().toString();

        if(emailId.trim().equals("")) {
            binding.userLoginIdEt.setError("An email ID is needed");
            return false;
        }
        if(password.trim().equals("")) {
            binding.userLoginPasswordEt.setError("Enter a password");
            return false;
        }
        if(confirmPassword.trim().equals("") || !confirmPassword.equals(password)) {
            binding.userConfirmPasswordEt.setError("Passwords do not match");
            return false;
        }

        return true;
    }

}
