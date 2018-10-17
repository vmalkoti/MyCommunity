package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.malkoti.capstone.mycommunity.databinding.FragmentViewMaintenanceReqInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewMaintenanceReqInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewMaintenanceReqInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMaintenanceReqInfo extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentViewMaintenanceReqInfoBinding binding;

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


    public ViewMaintenanceReqInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewMaintenanceReqInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewMaintenanceReqInfo newInstance() {
        ViewMaintenanceReqInfo fragment = new ViewMaintenanceReqInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_maintenance_req_info, container, false);

        populateDummyData();

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void populateDummyData() {
        binding.requestTitleTv.setText("Request Type");
        binding.requestUnitTv.setText("Apt #123");
        binding.requestResidentTv.setText("John Locke");
        binding.requestStatusTv.setText("Open");
        binding.requestDescTv.setText("Problem with the fridge. Leaking water");
        binding.requestCommentsTv.setText("-");
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


}
