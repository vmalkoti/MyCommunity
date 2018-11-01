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

import java.util.Arrays;
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
    private OnFragmentInteractionListener mListener;
    private FragmentEditMaintenanceDetailsBinding maintenanceDetailsBinding;
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
        //maintenanceDetailsBinding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_edit_maintenance_details);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        maintenanceDetailsBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_maintenance_details,
                container, false);

        //populateDummyData();
        initUI();

        return maintenanceDetailsBinding.getRoot();
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

        maintenanceDetailsBinding.newReqGroup.setEnabled(isNewRequest);
        maintenanceDetailsBinding.existingReqGroup.setEnabled(!isNewRequest);

        // disable fields to be automatically populated by code
        maintenanceDetailsBinding.requestStatus.setEnabled(false);
        //maintenanceDetailsBinding.requestUnitEt.setEnabled(false);
        maintenanceDetailsBinding.requestResidentEt.setEnabled(false);

        // extra field for future implementation
        maintenanceDetailsBinding.requestCommentsEt.setVisibility(View.GONE);

        maintenanceDetailsBinding.requestSubmitBtn.setOnClickListener(view -> {
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
        String type = maintenanceDetailsBinding.requestTitleEt.getText().toString().trim();
        String unit = maintenanceDetailsBinding.requestUnitEt.getText().toString().trim();
        String desc = maintenanceDetailsBinding.requestDescEt.getText().toString().trim();

        if(type.equals("")) {
            maintenanceDetailsBinding.requestTitleEt.setError("Required");
            return false;
        }
        if(unit.equals("")) {
            maintenanceDetailsBinding.requestUnitEt.setError("Required");
            return false;
        }
        if(desc.equals("")) {
            maintenanceDetailsBinding.requestDescEt.setError("Required");
            return false;
        }

        viewModel.getSelectedRequest().getValue().reqType = type;
        viewModel.getSelectedRequest().getValue().reqDescription = desc;

        return true;
    }


    private void autopopulateDefaults() {
        AppUser user = viewModel.getSignedInUser().getValue();
        // populate resident name
        maintenanceDetailsBinding.requestResidentEt.setText(user.fullName);

        // for future implementation - get apt name
    }

    /**
     * Test method to provide dummy data for initial testing
     */
    private void populateDummyData() {
        maintenanceDetailsBinding.requestTitleEt.setText("Electrical appliance not working");
        maintenanceDetailsBinding.requestUnitEt.setText("Apt #123");
        maintenanceDetailsBinding.requestResidentEt.setText("George Michael");
        maintenanceDetailsBinding.requestDescEt.setText("The fridge is not cooling. Tried plugging out and back in but that made no difference.");
        maintenanceDetailsBinding.requestCommentsEt.setText("Contacted Samsung for repairs.");

        List<String> statusList = Arrays.asList(getResources().getStringArray(R.array.request_status_list));
        String status = getString(R.string.req_status_vendor);
        maintenanceDetailsBinding.requestStatus.setSelection(statusList.indexOf(status));
    }

}
