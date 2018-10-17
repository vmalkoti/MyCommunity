package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewApartmentInfoBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewApartmentInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewApartmentInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewApartmentInfo extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentViewApartmentInfoBinding binding;

    public ViewApartmentInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewApartmentInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewApartmentInfo newInstance() {
        ViewApartmentInfo fragment = new ViewApartmentInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_apartment_info, container, false);

        populateDummyData();

        initUI();

        return binding.getRoot();
    }

    private void populateDummyData() {
        binding.aptNameTv.setText("Apt #123");
        binding.aptNumRoomsTv.setText("2");
        binding.aptStatusTv.setText("Occupied");
        binding.aptNotesTv.setText("Apartment has fridge, stove, microwave and washer-dryer. "
            + "The stove is gas operated. Flooring is hardwood and countertops are granite. "
            + "Bedroom is carpeted and has a large window. "
            + "Apartment has a patio accessible from the living area. "
            + "Kitchen has a small pantry and bar-style countertops near living area.");
    }

    private void initUI() {
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
