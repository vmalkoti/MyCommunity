package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.ListItemApartmentBinding;
import com.malkoti.capstone.mycommunity.databinding.FragmentViewApartmentsListBinding;
import com.malkoti.capstone.mycommunity.model.Apartment;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewApartmentsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewApartmentsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewApartmentsList extends Fragment {
    private OnFragmentInteractionListener interactionListener;
    private FragmentViewApartmentsListBinding binding;
    private ApartmentsListAdapter adapter;
    private MainViewModel viewModel;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    // Required empty public constructor
    public ViewApartmentsList() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewApartmentsList.
     */
    public static ViewApartmentsList newInstance() {
        ViewApartmentsList fragment = new ViewApartmentsList();
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
                R.layout.fragment_view_apartments_list, container, false);

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

    private void initUI() {
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        viewModel.getApartments().observe(this, new Observer<List<Apartment>>() {
            @Override
            public void onChanged(@Nullable List<Apartment> apartments) {
                adapter.setData(apartments);
            }
        });

        binding.apartmentsListRv.setHasFixedSize(true);
        binding.apartmentsListRv.setLayoutManager(new LinearLayoutManager(ViewApartmentsList.this.getContext()));
        binding.apartmentsListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new ApartmentsListAdapter(() -> interactionListener.onFragmentInteraction(null));
        //  adapter.setData(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        binding.apartmentsListRv.setAdapter(adapter);
    }


}


/**
 * Adapter class for list of apartments
 */
class ApartmentsListAdapter extends RecyclerView.Adapter<ApartmentsListAdapter.ApartmentViewHolder> {
    //private List<Integer> apartments;
    private List<Apartment> apartments;
    private OnApartmentClickListener listener;

    /**
     * Interface for click events on items shown by the adapter
     */
    interface OnApartmentClickListener {
        void onClick();
    }

    ApartmentsListAdapter(OnApartmentClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ApartmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemApartmentBinding binding = ListItemApartmentBinding.inflate(inflater, viewGroup, false);

        return new ApartmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentViewHolder viewHolder, int i) {
        viewHolder.bindView(apartments.get(i));
    }

    @Override
    public int getItemCount() {
        return apartments==null? 0: apartments.size();
    }

    /**
     * Method to pass data to the adapter
     * @param data
     */
    public void setData(List<Apartment> data) {
        this.apartments = data;
        notifyDataSetChanged();
    }


    /**
     * ViewHolder class for list of apartments
     */
    class ApartmentViewHolder extends RecyclerView.ViewHolder {
        private ListItemApartmentBinding itemBinding;

        ApartmentViewHolder(ListItemApartmentBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        /**
         * Bind data item to the viewholder
         * @param apt
         */
        void bindView(Apartment apt) {
            itemBinding.aptItemPrimaryText.setText(apt.aptName);
            String secondaryText = apt.numOfBedrooms + " bedrooms, " + apt.status;
            itemBinding.aptItemSecondaryText.setText(secondaryText);

            // set onclick listener for item
            itemBinding.aptItemContainer.setOnClickListener(view -> listener.onClick());

            itemBinding.executePendingBindings();
        }

    }
}