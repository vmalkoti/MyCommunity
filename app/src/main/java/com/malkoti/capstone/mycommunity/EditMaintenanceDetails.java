package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentEditMaintenanceDetailsBinding;

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
    private boolean isNewRequest;
    private static final String ARG_PARAM1 = "IS_NEW_REQ";

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

        populateDummyData();
        initUI();

        return maintenanceDetailsBinding.getRoot();
    }

    /**
     *
     */
    private void initUI() {
        maintenanceDetailsBinding.newReqGroup.setEnabled(isNewRequest);
        maintenanceDetailsBinding.existingReqGroup.setEnabled(!isNewRequest);
    }

    /**
     *
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
        void onFragmentInteraction(Uri uri);
    }
}
