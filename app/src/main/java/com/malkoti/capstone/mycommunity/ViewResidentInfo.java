package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewResidentInfoBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewResidentInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewResidentInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewResidentInfo extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentViewResidentInfoBinding viewResidentInfoBinding;

    public ViewResidentInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ViewResidentInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewResidentInfo newInstance() {
        ViewResidentInfo fragment = new ViewResidentInfo();
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
        //viewResidentInfoBinding = DataBindingUtil.setContentView(this.getActivity(), R.layout.fragment_view_resident_info);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewResidentInfoBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_resident_info,
                container, false);

        populateDummyData();

        return viewResidentInfoBinding.getRoot();
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

    private void populateDummyData() {
        viewResidentInfoBinding.residentFnameTv.setText("Forrest");
        viewResidentInfoBinding.residentLnameTv.setText("Gump");
        viewResidentInfoBinding.residentGenderTv.setText("Male");
        viewResidentInfoBinding.residentEmailTv.setText("forrest@bubbagump.com");
        viewResidentInfoBinding.residentPhoneTv.setText("(555) 555-1234");
        viewResidentInfoBinding.residentPhoto.setImageDrawable(this.getResources().getDrawable(R.drawable.icons8_person_male_80));
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
