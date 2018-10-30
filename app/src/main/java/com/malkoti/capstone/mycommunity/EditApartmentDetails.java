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

import com.malkoti.capstone.mycommunity.databinding.FragmentEditApartmentDetailsBinding;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditApartmentDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditApartmentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditApartmentDetails extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentEditApartmentDetailsBinding binding;
    private DetailsViewModel viewModel;


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public EditApartmentDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditApartmentDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static EditApartmentDetails newInstance() {
        EditApartmentDetails fragment = new EditApartmentDetails();
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
                R.layout.fragment_edit_apartment_details, container, false);

        initUI();

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * Initialize UI componenets
     */
    private void initUI() {
        viewModel = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);

        binding.submitBtn.setOnClickListener(v -> {
            if (fieldsVerified()) {
                mListener.onFragmentInteraction(null);
            }
        });
    }

    /**
     * Verify field inputs
     * @return True if all fields have valid values, else false
     */
    private boolean fieldsVerified() {
        String name = binding.aptNameEt.getText().toString().trim();
        int rooms = Integer.parseInt(binding.aptNumRoomsEt.getText().toString());
        String status = binding.aptStatusSp.getSelectedItem().toString();
        String notes = binding.aptNotesEt.getText().toString().trim();

        if(name.equals("")) {
            binding.aptNameEt.setError("Required");
            return false;
        }
        if(rooms<=0) {
            binding.aptNumRoomsEt.setError("Invalid");
            return false;
        }
        // notes is optional

        viewModel.getSelectedApartment().getValue().aptName = name;
        viewModel.getSelectedApartment().getValue().numOfBedrooms = rooms;
        viewModel.getSelectedApartment().getValue().status = status;
        viewModel.getSelectedApartment().getValue().aptDescription = notes;

        //Apartment apt = new Apartment(name, rooms, status, "", notes);
        //viewModel.setSelectedApartment(apt);

        return true;
    }
}
