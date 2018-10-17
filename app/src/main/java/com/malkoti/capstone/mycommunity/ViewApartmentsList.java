package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
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
    private OnFragmentInteractionListener mListener;
    private FragmentViewApartmentsListBinding binding;
    private ApartmentsListAdapter adapter;

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
                R.layout.fragment_view_apartments_list, container, false);

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
        binding.apartmentsListRv.setHasFixedSize(true);
        binding.apartmentsListRv.setLayoutManager(new LinearLayoutManager(ViewApartmentsList.this.getContext()));
        binding.apartmentsListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new ApartmentsListAdapter(() -> mListener.onFragmentInteraction(null));
        adapter.setData(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        binding.apartmentsListRv.setAdapter(adapter);
    }


}


/**
 * Adapter class for list of apartments
 */
class ApartmentsListAdapter extends RecyclerView.Adapter<ApartmentsListAdapter.ApartmentViewHolder> {
    private List<Integer> apartments;
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
    public void setData(List<Integer> data) {
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
         * @param val
         */
        void bindView(int val) {
            itemBinding.aptItemPrimaryText.setText("Apartment " + val);
            itemBinding.aptItemSecondaryText.setText("Occupied");

            // set onclick listener for item
            itemBinding.aptItemContainer.setOnClickListener(view -> listener.onClick());

            itemBinding.executePendingBindings();
        }

    }
}