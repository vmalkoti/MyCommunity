package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.malkoti.capstone.mycommunity.databinding.FragmentViewMaintenanceReqInfoBinding;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewMaintenanceReqInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMaintenanceReqInfo extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + ViewMaintenanceReqInfo.class.getSimpleName();

    private FragmentViewMaintenanceReqInfoBinding binding;
    private DetailsViewModel viewModel;


    public ViewMaintenanceReqInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewMaintenanceReqInfo.
     */
    public static ViewMaintenanceReqInfo newInstance() {
        ViewMaintenanceReqInfo fragment = new ViewMaintenanceReqInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_maintenance_req_info,
                container, false);

        initUI();

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    /**
     * Initialize UI components
     */
    private void initUI() {
        MaintenanceRequest request = viewModel.getSelectedRequest().getValue();
        if(request != null) {
            Log.d(LOG_TAG, "initUI: populating views with viewmodel data");
            binding.requestTitleTv.setText(request.reqType);
            binding.requestUnitTv.setText(request.aptId);
            binding.requestResidentTv.setText(request.residentId);
            binding.requestStatusTv.setText(request.reqStatus);
            binding.requestDescTv.setText(request.reqDescription);
            binding.requestCommentsTv.setText(request.reqComments);
        } else {
            Log.d(LOG_TAG, "initUI: viewmodel data is null");
        }
    }
}
