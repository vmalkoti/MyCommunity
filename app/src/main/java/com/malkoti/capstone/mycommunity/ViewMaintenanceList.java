package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
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

import com.malkoti.capstone.mycommunity.databinding.FragmentViewMaintenanceListBinding;
import com.malkoti.capstone.mycommunity.databinding.ListItemRequestBinding;
import com.malkoti.capstone.mycommunity.model.MaintenanceRequest;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewMaintenanceList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewMaintenanceList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewMaintenanceList extends Fragment {
    private static final String LOG_TAG = "DEBUG_" + ViewMaintenanceList.class.getSimpleName();

    private OnFragmentInteractionListener interactionListener;
    private FragmentViewMaintenanceListBinding binding;
    private RequestsListAdapter adapter;
    private MainViewModel viewModel;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Parcelable selectedItem);
    }


    public ViewMaintenanceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewMaintenanceList.
     */
    public static ViewMaintenanceList newInstance() {
        ViewMaintenanceList fragment = new ViewMaintenanceList();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_maintenance_list,
                container, false);

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
        adapter = new RequestsListAdapter(viewModel,
                (request) -> interactionListener.onFragmentInteraction(request));

        viewModel.getRequests().observe(getActivity(), requests -> adapter.setData(requests));

        binding.requestListRv.setHasFixedSize(true);
        binding.requestListRv.setLayoutManager(new LinearLayoutManager(ViewMaintenanceList.this.getContext()));
        binding.requestListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.requestListRv.setAdapter(adapter);
    }
}


/**
 * Adapter class for maintenance request recyclerview
 */
class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.RequestViewHolder> {
    private static final String LOG_TAG = "DEBUG_" + RequestsListAdapter.class.getSimpleName();

    private List<MaintenanceRequest> requests;
    private OnMaintenanceRequestClickListener listener;
    private MainViewModel viewModel;

    /**
     * Interface for click events on items shown by the adapter
     */
    interface OnMaintenanceRequestClickListener {
        void onItemClick(MaintenanceRequest request);
    }

    RequestsListAdapter(MainViewModel viewModel, OnMaintenanceRequestClickListener listener) {
        this.viewModel = viewModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemRequestBinding binding = ListItemRequestBinding.inflate(inflater, viewGroup, false);

        return new RequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder viewHolder, int i) {
        viewHolder.bindView(requests.get(i));
    }

    @Override
    public int getItemCount() {
        return requests==null? 0 : requests.size();
    }

    /**
     * Method to pass data to the adapter
     * @param data
     */
    public void setData(List<MaintenanceRequest> data) {
        Log.d(LOG_TAG, "setData: New data received: " + (data==null ? -1: data.size()));

        this.requests = data;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for list of maintenance requests
     */
    class RequestViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        private ListItemRequestBinding itemBinding;
        private LifecycleRegistry lifecycleRegistry;


        RequestViewHolder(ListItemRequestBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;

            lifecycleRegistry = new LifecycleRegistry(this);
            lifecycleRegistry.markState(Lifecycle.State.CREATED);
        }

        /**
         *
         * @param request
         */
        void bindView(MaintenanceRequest request) {
            lifecycleRegistry.markState(Lifecycle.State.CREATED);

            itemBinding.reqItemType.setText(request.reqType);
            itemBinding.reqItemDate.setText(request.reqDate);

            viewModel.getApartmentById(request.aptId)
                    .observe(this, apt -> itemBinding.reqItemApt.setText(apt.aptName));

            itemBinding.reqItemContainer.setOnClickListener(view -> listener.onItemClick(request));

            itemBinding.executePendingBindings();
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycleRegistry;
        }
    }
}