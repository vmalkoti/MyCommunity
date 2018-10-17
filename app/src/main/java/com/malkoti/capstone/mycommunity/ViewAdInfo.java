package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewAdInfoBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewAdInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewAdInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAdInfo extends Fragment {
    private OnFragmentInteractionListener interactionListener;
    private FragmentViewAdInfoBinding binding;

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

    public ViewAdInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewAdInfo.
     */
    public static ViewAdInfo newInstance() {
        ViewAdInfo fragment = new ViewAdInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_ad_info,
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
        binding.adPostTitleTv.setText("Yard sale, everything $10!!!");
        binding.adPostResidentTv.setText("Jacob");
        binding.adPostDateTv.setText("10-15-2018");
        binding.adPostDescTv.setText("I'm moving to a different island and want to get rid of all the stuff. "
                + "I'm selling everything from furniture to appliances. "
                + "First come, first serve. Hurry up."
                + "Contact +1-888-555-1234 for details.");

    }
}
