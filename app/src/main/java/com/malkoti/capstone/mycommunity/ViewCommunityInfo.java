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

import com.malkoti.capstone.mycommunity.databinding.FragmentViewCommunityBinding;
import com.malkoti.capstone.mycommunity.model.Community;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewCommunityInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewCommunityInfo extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + ViewCommunityInfo.class.getSimpleName();

    private FragmentViewCommunityBinding viewCommunityBinding;
    private DetailsViewModel viewModel;


    public ViewCommunityInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ViewCommunityInfo.
     */
    public static ViewCommunityInfo newInstance() {
        ViewCommunityInfo fragment = new ViewCommunityInfo();
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
        viewCommunityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_community,
                container, false);

        initUI();

        return viewCommunityBinding.getRoot();

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
     * Initialize UI components
     */
    private void initUI() {
        Community community = viewModel.getSelectedCommunity().getValue();
        if(community != null) {
            Log.d(LOG_TAG, "initUI: populating views with viewmodel data");
            viewCommunityBinding.communityNameTv.setText(community.name);
            viewCommunityBinding.communityDescTv.setText(community.description);
            viewCommunityBinding.communityStreetAddressEt.setText(community.streetAddress);
            viewCommunityBinding.communityCityEt.setText(community.city);
            viewCommunityBinding.communityStateEt.setText(community.state);
            viewCommunityBinding.communityZipEt.setText(community.zip);
            viewCommunityBinding.communityCountryEt.setText(community.country);
            viewCommunityBinding.communityPhoneEt.setText(community.phoneNum);

            // static image for now
            // future implementation - show images uploaded to firebase storage
            viewCommunityBinding.communityPhoto.setImageDrawable(this.getResources().getDrawable(R.drawable.houses_img_placeholder));
        } else {
            Log.d(LOG_TAG, "initUI: viewmodel data is null");
        }
    }

}
