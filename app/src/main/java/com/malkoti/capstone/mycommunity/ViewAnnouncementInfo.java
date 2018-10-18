package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewAnnouncementInfoBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewAnnouncementInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewAnnouncementInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAnnouncementInfo extends Fragment {
    private OnFragmentInteractionListener interactionListener;
    private FragmentViewAnnouncementInfoBinding binding;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_announcement_info,
                container, false);
        populateDummyData();

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
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
        interactionListener = null;
    }

    /**
     *
     */
    private void populateDummyData() {
        binding.adPostTitleTv.setText("Fitness center renovation");
        binding.adPostResidentTv.setText("Management Management");
        binding.adPostDateTv.setText("10-15-2018");
        binding.adPostDescTv.setText("The gym will be closed for repair and renovation work "
                + "from 10/01/2018 to 10/31/2018 to service machines, replace carpeting and mirrors, "
                + "and for safety inspection of all machines.");

    }
}
