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

import com.malkoti.capstone.mycommunity.databinding.FragmentViewAnnouncementInfoBinding;
import com.malkoti.capstone.mycommunity.model.AnnouncementPost;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewAnnouncementInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAnnouncementInfo extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + ViewAnnouncementInfo.class.getSimpleName();

    private FragmentViewAnnouncementInfoBinding binding;
    private DetailsViewModel viewModel;


    public ViewAnnouncementInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewAnnouncementInfo.
     */
    public static ViewAnnouncementInfo newInstance() {
        ViewAnnouncementInfo fragment = new ViewAnnouncementInfo();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_announcement_info,
                container, false);

        initUI();

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {

            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //interactionListener = null;
    }

    /**
     * Initialize UI controls
     */
    private void initUI() {
        AnnouncementPost post = viewModel.getSelectedAnnouncement().getValue();
        if (post != null) {
            Log.d(LOG_TAG, "initUI: populating views with viewmodel data");
            binding.adPostTitleTv.setText(post.announcementTitle);
            // for future implementation - set visible and show management name
            binding.adPostResidentTv.setVisibility(View.GONE);
            binding.adPostDateTv.setText(post.postDate);
            binding.adPostDescTv.setText(post.postDescription);
        } else {
            Log.d(LOG_TAG, "initUI: viewmodel data is null");
        }
    }

}
