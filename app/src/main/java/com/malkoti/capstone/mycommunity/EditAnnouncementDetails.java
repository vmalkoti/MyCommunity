package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentEditAnnouncementDetailsBinding;
import com.malkoti.capstone.mycommunity.viewmodels.DetailsViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditAnnouncementDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditAnnouncementDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAnnouncementDetails extends Fragment {
    private OnFragmentInteractionListener mListener;
    private FragmentEditAnnouncementDetailsBinding binding;
    private DetailsViewModel viewModel;

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

    public EditAnnouncementDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditAnnouncementDetails.
     */
    public static EditAnnouncementDetails newInstance() {
        EditAnnouncementDetails fragment = new EditAnnouncementDetails();
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
                R.layout.fragment_edit_announcement_details, container, false);

        initUI();

        return binding.getRoot();
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


    private void initUI() {
        viewModel = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);

        binding.requestSubmitBtn.setOnClickListener(v -> {
            if (fieldsVerified()) {
                mListener.onFragmentInteraction(null);
            }
        });
    }

    private boolean fieldsVerified() {
        String title = binding.adPostTitleEt.getText().toString().trim();
        String desc = binding.requestDescEt.getText().toString().trim();

        if(title.equals("")) {
            binding.adPostTitleEt.setError("Required");
            return false;
        }
        if (desc.equals("")) {
            binding.requestDescEt.setError("Required");
            return false;
        }

        //String date = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss").format(new Date());
        String date = DateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL)
                .format(Calendar.getInstance().getTime());

        viewModel.getSelectedAnnouncement().getValue().announcementTitle = title;
        viewModel.getSelectedAnnouncement().getValue().postDescription = desc;
        viewModel.getSelectedAnnouncement().getValue().postDate = date;

        return true;
    }

}

