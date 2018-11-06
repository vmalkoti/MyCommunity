package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentEditCommunityDetailsBinding;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditCommunityDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditCommunityDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCommunityDetails extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentEditCommunityDetailsBinding binding;
    private DetailsViewModel viewModel;


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public EditCommunityDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditCommunityDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCommunityDetails newInstance() {
        EditCommunityDetails fragment = new EditCommunityDetails();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_community_details, container, false);
        initUI();

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        mListener = null;
    }

    /**
     * Initialize UI components
     */
    private void initUI() {
        viewModel = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);

        binding.addCommunityBtn.setOnClickListener(view -> {
            if(fieldsVerified()) {
                mListener.onFragmentInteraction(null);
            }
        });
    }

    /**
     * Verify field inputs
     * @return True if all fields have valid values, else false
     */
    private boolean fieldsVerified() {
        final String name = binding.editCommunityEntryFields.communityNameEt.getText().toString().trim();
        final String description = binding.editCommunityEntryFields.communityDescEt.getText().toString().trim();
        final String streetAddress = binding.editCommunityEntryFields.communityStreetAddressEt.getText().toString().trim();
        final String city = binding.editCommunityEntryFields.communityCityEt.getText().toString().trim();
        final String state = binding.editCommunityEntryFields.communityStateEt.getText().toString().trim();
        final String zip = binding.editCommunityEntryFields.communityZipEt.getText().toString().trim();
        final String country = binding.editCommunityEntryFields.communityCountryEt.getText().toString().trim();
        final String phone = binding.editCommunityEntryFields.communityPhoneEt.getText().toString().trim();

        String reqFieldMsg = getString(R.string.required_field_error_msg);

        if (name.equals("")) {
            binding.editCommunityEntryFields.communityNameEt.setError(reqFieldMsg);
            return false;
        }
        if (streetAddress.equals("")) {
            binding.editCommunityEntryFields.communityStreetAddressEt.setError(reqFieldMsg);
            return false;
        }
        if (city.equals("")) {
            binding.editCommunityEntryFields.communityCityEt.setError(reqFieldMsg);
            return false;
        }
        if (state.equals("")) {
            binding.editCommunityEntryFields.communityStateEt.setError(reqFieldMsg);
            return false;
        }
        if (zip.equals("")) {
            binding.editCommunityEntryFields.communityZipEt.setError(reqFieldMsg);
            return false;
        }
        if (phone.equals("")) {
            binding.editCommunityEntryFields.communityPhoneEt.setError(reqFieldMsg);
            return false;
        }
        // description and country are optional

        // pass values to viewmodel
        viewModel.getSelectedCommunity().getValue().name = name;
        viewModel.getSelectedCommunity().getValue().streetAddress = streetAddress;
        viewModel.getSelectedCommunity().getValue().city = city;
        viewModel.getSelectedCommunity().getValue().state = state;
        viewModel.getSelectedCommunity().getValue().zip = zip;
        viewModel.getSelectedCommunity().getValue().phoneNum = phone;
        viewModel.getSelectedCommunity().getValue().description = description;
        viewModel.getSelectedCommunity().getValue().country = country;

        return true;
    }

}
