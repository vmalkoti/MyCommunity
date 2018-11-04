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

import com.malkoti.capstone.mycommunity.databinding.FragmentViewApartmentInfoBinding;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewApartmentInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewApartmentInfo extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + ViewApartmentInfo.class.getSimpleName();

    private FragmentViewApartmentInfoBinding binding;
    private DetailsViewModel viewModel;

    public ViewApartmentInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewApartmentInfo.
     */
    public static ViewApartmentInfo newInstance() {
        ViewApartmentInfo fragment = new ViewApartmentInfo();
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
                R.layout.fragment_view_apartment_info, container, false);

        initUI();

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    /**
     *
     */
    private void initUI() {
        Apartment apt = viewModel.getSelectedApartment().getValue();
        if(apt!=null) {
            Log.d(LOG_TAG, "populateViews: populating views with viewmodel data");
            binding.aptNameTv.setText(apt.aptName);
            String numRooms = apt.numOfBedrooms + " bedrooms";
            binding.aptNumRoomsTv.setText(numRooms);
            binding.aptStatusTv.setText(apt.status);
            binding.aptNotesTv.setText(apt.aptDescription);
        } else {
            Log.d(LOG_TAG, "populateViews: viewmodel data is null");
        }
    }

}
