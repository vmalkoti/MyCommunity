package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewResidentsListBinding;
import com.malkoti.capstone.mycommunity.databinding.ListItemResidentBinding;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewResidentsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewResidentsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewResidentsList extends Fragment {
    private OnFragmentInteractionListener interactionListener;
    private FragmentViewResidentsListBinding binding;
    private ResidentListAdapter adapter;
    private MainViewModel viewModel;

    public ViewResidentsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewResidentsList.
     */
    public static ViewResidentsList newInstance() {
        ViewResidentsList fragment = new ViewResidentsList();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_residents_list, container, false);

        initUI();

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onFragmentInteraction(uri);
        }
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
    private void initUI() {
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        adapter = new ResidentListAdapter(() -> interactionListener.onFragmentInteraction(null));

        viewModel.getResidents().observe(getActivity(), new Observer<List<AppUser>>() {
            @Override
            public void onChanged(@Nullable List<AppUser> appUsers) {
                adapter.setData(appUsers);
            }
        });
        binding.residentsListRv.setHasFixedSize(true);
        binding.residentsListRv.setLayoutManager(new LinearLayoutManager(ViewResidentsList.this.getContext()));
        binding.residentsListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        //adapter.setData(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        binding.residentsListRv.setAdapter(adapter);
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


/**
 *
 */
class ResidentListAdapter extends RecyclerView.Adapter<ResidentListAdapter.ResidentViewHolder> {
    List<AppUser> residents;
    private OnResidentClickListener listener;

    /**
     * Interface for click events on items shown by the adapter
     */
    interface OnResidentClickListener {
        void onClick();
    }

    ResidentListAdapter(OnResidentClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResidentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemResidentBinding binding =  ListItemResidentBinding.inflate(inflater, viewGroup, false);

        return new ResidentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidentViewHolder viewHolder, int i) {
        viewHolder.bindView(residents.get(i));
    }

    @Override
    public int getItemCount() {
        return residents==null ? 0: residents.size();
    }

    /**
     * Method to pass data to the adapter
     * @param data
     */
    public void setData(List<AppUser> data) {
        this.residents = data;
    }


    /**
     * ViewHolder class for list of apartments
     */
    class ResidentViewHolder extends RecyclerView.ViewHolder {
        private ListItemResidentBinding itemBinding;

        ResidentViewHolder(ListItemResidentBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        /**
         * Bind data item to the viewholder
         * @param resident
         */
        void bindView(AppUser resident) {
            itemBinding.residentItemFname.setText(resident.fullName);
            //itemBinding.residentItemLname.setText("Locke");
            itemBinding.residentItemAptName.setText(resident.aptId);
            if (resident.gender.toLowerCase().equals("female")) {
                itemBinding.residentItemImg.setImageDrawable(itemBinding.residentItemImg
                        .getContext().getResources()
                        .getDrawable(R.drawable.icons8_person_female_80));
            } else {
                itemBinding.residentItemImg.setImageDrawable(itemBinding.residentItemImg
                        .getContext().getResources()
                        .getDrawable(R.drawable.icons8_person_male_80));
            }

            // set onclick listener for item
            itemBinding.residentItemContainer.setOnClickListener(view -> listener.onClick());

            itemBinding.executePendingBindings();
        }
    }
}