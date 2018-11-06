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

import com.malkoti.capstone.mycommunity.databinding.FragmentEditMaintenanceDetailsBinding;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditMaintenanceDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditMaintenanceDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditMaintenanceDetails extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + EditMaintenanceDetails.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private FragmentEditMaintenanceDetailsBinding binding;
    private DetailsViewModel viewModel;

    private boolean isNewRequest;
    private static final String ARG_PARAM1 = "IS_NEW_REQ";

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public EditMaintenanceDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditMaintenanceDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static EditMaintenanceDetails newInstance(boolean isNewRequest) {
        EditMaintenanceDetails fragment = new EditMaintenanceDetails();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isNewRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNewRequest = getArguments().getBoolean(ARG_PARAM1);
        }
        //binding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_edit_maintenance_details);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_maintenance_details,
                container, false);

        //populateDummyData();
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

        binding.newReqGroup.setEnabled(isNewRequest);
        binding.existingReqGroup.setEnabled(!isNewRequest);

        // disable fields to be automatically populated by code
        binding.requestStatus.setEnabled(false);
        //binding.requestUnitEt.setEnabled(false);
        binding.requestResidentEt.setEnabled(false);

        // extra field for future implementation
        binding.requestCommentsEt.setVisibility(View.GONE);

        binding.requestSubmitBtn.setOnClickListener(view -> {
            if(fieldsVerified()) {
                mListener.onFragmentInteraction(null);
            }
        });

        autopopulateDefaults();
    }



    /**
     * Autopopulate fields with default values
     */
    private void autopopulateDefaults() {
        // autopopulated via code
        binding.requestResidentEt.setEnabled(false);
        binding.requestUnitEt.setEnabled(false);

        // for future implementation - get apt name
        viewModel.getSignedInUser().observe(this, appUser -> {
            binding.requestResidentEt.setText(appUser.fullName);
            viewModel.getSelectedRequest().getValue().residentId = appUser.userKey;
            viewModel.getSelectedRequest().getValue().aptId = appUser.aptId;
            viewModel.getSelectedRequest().getValue().communityId = appUser.communityId;

            viewModel.getApartmentById(appUser.aptId).observe(EditMaintenanceDetails.this, apartment -> {
                binding.requestUnitEt.setText(apartment.aptName);
            });
        });

    }


    /**
     * Verify field inputs
     * @return True if all fields have valid values, else false
     */
    private boolean fieldsVerified() {
        String type = binding.requestTitleEt.getText().toString().trim();
        String unit = binding.requestUnitEt.getText().toString().trim();
        String desc = binding.requestDescEt.getText().toString().trim();

        String reqFieldMsg = getString(R.string.required_field_error_msg);

        if(type.equals("")) {
            binding.requestTitleEt.setError(reqFieldMsg);
            return false;
        }
        if(unit.equals("")) {
            binding.requestUnitEt.setError(reqFieldMsg);
            return false;
        }
        if(desc.equals("")) {
            binding.requestDescEt.setError(reqFieldMsg);
            return false;
        }

        String date = DateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL)
                .format(Calendar.getInstance().getTime());

        viewModel.getSelectedRequest().getValue().reqDate = date;
        viewModel.getSelectedRequest().getValue().reqType = type;
        viewModel.getSelectedRequest().getValue().reqDescription = desc;

        return true;
    }



    /**
     * Test method to provide dummy data for initial testing
     */
    private void populateDummyData() {
        binding.requestTitleEt.setText("Electrical appliance not working");
        binding.requestUnitEt.setText("Apt #123");
        binding.requestResidentEt.setText("George Michael");
        binding.requestDescEt.setText("The fridge is not cooling. Tried plugging out and back in but that made no difference.");
        binding.requestCommentsEt.setText("Contacted Samsung for repairs.");

        List<String> statusList = Arrays.asList(getResources().getStringArray(R.array.request_status_list));
        String status = getString(R.string.req_status_vendor);
        binding.requestStatus.setSelection(statusList.indexOf(status));
    }

}
