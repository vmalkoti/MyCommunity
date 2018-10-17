package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewCommunityBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewCommunityInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewCommunityInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewCommunityInfo extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentViewCommunityBinding viewCommunityBinding;


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
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
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
        //viewCommunityBinding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_view_community);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewCommunityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_community,
                container, false);

        populateDummyData();

        return viewCommunityBinding.getRoot();

    }

    private void populateDummyData() {
        viewCommunityBinding.communityNameTv.setText("Woodside Terrace");
        viewCommunityBinding.communityDescTv.setText("Some random text describing this community, "
                + "highlighting its features, facilities and other details user may find useful."
        + "Adding some more text here to increase scroll size "
        + "to be able to test that the scrolling works on this screen."
        + "About 4-6 lines of text should be enough to fill the screen for scrolling."
        + "Which means addition of this line should do the trick, and make the text "
        + "long enough to start scrolling on the screen.");
        viewCommunityBinding.communityStreetAddressEt.setText("400 N Chapel Ave");
        viewCommunityBinding.communityCityEt.setText("Alhambra");
        viewCommunityBinding.communityStateEt.setText("CA");
        viewCommunityBinding.communityZipEt.setText("91801");
        viewCommunityBinding.communityCountryEt.setText("US");
        viewCommunityBinding.communityPhoneEt.setText("(800) 555-5555");
        viewCommunityBinding.communityPhoto.setImageDrawable(this.getResources().getDrawable(R.drawable.houses_img_placeholder));
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
