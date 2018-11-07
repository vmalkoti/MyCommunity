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

import com.bumptech.glide.Glide;
import com.malkoti.capstone.mycommunity.databinding.FragmentViewResidentInfoBinding;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewResidentInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewResidentInfo extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + ViewResidentInfo.class.getSimpleName();

    private FragmentViewResidentInfoBinding binding;
    private DetailsViewModel viewModel;

    public ViewResidentInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewResidentInfo.
     */
    public static ViewResidentInfo newInstance() {
        ViewResidentInfo fragment = new ViewResidentInfo();
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
                R.layout.fragment_view_resident_info,
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
        AppUser resident = viewModel.getSelectedUser().getValue();
        if (resident != null) {
            Log.d(LOG_TAG, "initUI: populating views with viewmodel data");
            binding.residentFnameTv.setText(resident.fullName);
            // for future implementation - get name as fname and lname
            binding.residentLnameTv.setVisibility(View.GONE);
            binding.residentGenderTv.setText(resident.gender);
            binding.residentEmailTv.setText(resident.email);
            binding.residentPhoneTv.setText(resident.phoneNum);

            // static image for now
            // future implementation - show images uploaded to firebase storage
            int img;
            if (resident.gender.toLowerCase().equals("female")) {
                img = R.drawable.icons8_person_female_80;
                //binding.residentPhoto.setImageDrawable(this.getResources().getDrawable(R.drawable.icons8_person_female_80));
            } else {
                img = R.drawable.icons8_person_male_80;
                //binding.residentPhoto.setImageDrawable(this.getResources().getDrawable(R.drawable.icons8_person_male_80));
            }
            Glide.with(binding.residentPhoto).load(img).into(binding.residentPhoto);

            viewModel.getApartmentById(resident.aptId)
                    .observe(this, apartment -> binding.residentAptTv.setText(apartment.aptName));
        } else {
            Log.d(LOG_TAG, "initUI: viewmodel data is null");
        }
    }


}
