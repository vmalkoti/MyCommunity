package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewResidentsListBinding;
import com.malkoti.capstone.mycommunity.databinding.ListItemResidentBinding;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.model.AppUser;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

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
    private static final String LOG_TAG = "DEBUG_" + ViewResidentsList.class.getSimpleName();

    private OnFragmentInteractionListener interactionListener;
    private FragmentViewResidentsListBinding binding;
    private ResidentListAdapter adapter;
    private MainViewModel viewModel;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Parcelable selectedItem);
    }


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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_view_residents_list, container, false);

        initUI();

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
     * Initialize UI components
     */
    private void initUI() {
        adapter = new ResidentListAdapter(viewModel,
                (resident) -> interactionListener.onFragmentInteraction(resident));

        viewModel.getResidents().observe(getActivity(), residents -> adapter.setData(residents));

        binding.residentsListRv.setHasFixedSize(true);
        binding.residentsListRv.setLayoutManager(new LinearLayoutManager(ViewResidentsList.this.getContext()));
        binding.residentsListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.residentsListRv.setAdapter(adapter);
    }
}


/**
 * Adapter class for resident list recyclerview
 */
class ResidentListAdapter extends RecyclerView.Adapter<ResidentListAdapter.ResidentViewHolder> {
    private static final String LOG_TAG = "DEBUG_" + ResidentListAdapter.class.getSimpleName();

    private List<AppUser> residents;
    private OnResidentClickListener listener;
    private MainViewModel viewModel;

    /**
     * Interface for click events on items shown by the adapter
     */
    interface OnResidentClickListener {
        void onItemClick(AppUser resident);
    }

    ResidentListAdapter(MainViewModel viewModel, OnResidentClickListener listener) {
        this.viewModel = viewModel;
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
        Log.d(LOG_TAG, "setData: New data received: " + (data==null ? -1: data.size()));

        this.residents = data;
        notifyDataSetChanged();
    }


    /**
     * ViewHolder class for list of apartments.
     * Implements LifeCycleOwner to observe apartment livedata.
     */
    class ResidentViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        private ListItemResidentBinding itemBinding;
        private LifecycleRegistry lifecycleRegistry;

        ResidentViewHolder(ListItemResidentBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;

            lifecycleRegistry = new LifecycleRegistry(this);
            lifecycleRegistry.markState(Lifecycle.State.CREATED);
        }

        /**
         * Bind data item to the viewholder
         * @param resident
         */
        void bindView(AppUser resident) {
            lifecycleRegistry.markState(Lifecycle.State.STARTED);

            itemBinding.residentItemFname.setText(resident.fullName);
            //itemBinding.residentItemLname.setText("Locke");


            viewModel.getApartmentById(resident.aptId)
                    .observe(this, apt -> itemBinding.residentItemAptName.setText(apt.aptName));

            // static image for now
            // future implementation - show images uploaded to firebase storage
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
            itemBinding.residentItemContainer.setOnClickListener(view -> listener.onItemClick(resident));

            itemBinding.executePendingBindings();
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycleRegistry;
        }
    }
}