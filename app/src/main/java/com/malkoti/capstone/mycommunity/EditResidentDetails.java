package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.malkoti.capstone.mycommunity.databinding.FragmentEditResidentDetailsBinding;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditResidentDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditResidentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditResidentDetails extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + EditResidentDetails.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private FragmentEditResidentDetailsBinding binding;
    private DetailsViewModel viewModel;
    private List<Apartment> apartmentsList = new ArrayList<>();

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public EditResidentDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditResidentDetails.
     */
    public static EditResidentDetails newInstance() {
        EditResidentDetails fragment = new EditResidentDetails();
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
                R.layout.fragment_edit_resident_details, container, false);

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
     *
     */
    private void initUI() {
        viewModel = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);

        viewModel.getSignedInUser().observe(this, appUser -> populateAptDropdown(appUser.communityId));

        binding.addResidentBtn.setOnClickListener(view -> {
            if(fieldsVerified()) {
                mListener.onFragmentInteraction(null);
            }
        });
    }

    /**
     *
     * @return
     */
    private boolean fieldsVerified() {
        String fname = binding.residentFnameEt.getText().toString().trim();
        String lName = binding.residentLnameEt.getText().toString().trim();
        String gender = binding.residentGenderSpn.getSelectedItem().toString();
        String apt = binding.residentAptSpn.getSelectedItem().toString();
        String email = binding.residentEmailIdEt.getText().toString().trim();
        String phone = binding.residentPhoneEt.getText().toString().trim();

        if(fname.equals("")) {
            binding.residentFnameEt.setError("Required");
            return false;
        }
        if (lName.equals("")) {
            binding.residentLnameEt.setError("Required");
            return false;
        }

        if (gender==null || gender.equals("")) {
            ((TextView) binding.residentGenderSpn.getSelectedView()).setError("Required");
            return false;
        }
        if(apt==null || apt.equals("")) {
            ((TextView) binding.residentAptSpn.getSelectedView()).setError("Required");
            return false;
        }

        if (email.equals("")) {
            binding.residentEmailIdEt.setError("Required");
            return false;
        }
        if(phone.equals("")) {
            binding.residentPhoneEt.equals("");
            return false;
        }

        // set resident indicator
        viewModel.getSelectedUser().getValue().isManager = false;

        viewModel.getSelectedUser().getValue().fullName = fname + " " + lName;
        viewModel.getSelectedUser().getValue().gender = gender;
        viewModel.getSelectedUser().getValue().email = email;
        viewModel.getSelectedUser().getValue().phoneNum = phone;

        // need to get apt id
        viewModel.getSelectedUser().getValue().aptId = apartmentsList
                .get(binding.residentAptSpn.getSelectedItemPosition()).aptKey;

        // set new resident's community id to manager's community
        viewModel.getSelectedUser().getValue().communityId = viewModel.getSignedInUser().getValue().communityId;
        viewModel.getSelectedUser().getValue().mgmtId = viewModel.getSignedInUser().getValue().mgmtId;


        return true;
    }

    /**
     *
     */
    private void populateAptDropdown(String communityId) {
        viewModel.getApartmentListOfCommunity(communityId).observe(this, apartments -> {
            //apartmentsList.clear();
            apartmentsList = apartments;
        });
        ArrayAdapter<Apartment> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, apartmentsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.residentAptSpn.setAdapter(adapter);
        binding.residentAptSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
                Log.d(LOG_TAG, "populateAptDropdown: item selected " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(LOG_TAG, "populateAptDropdown: nothing selected");
            }
        });
    }

}
