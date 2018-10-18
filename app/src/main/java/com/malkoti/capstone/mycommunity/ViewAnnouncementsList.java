package com.malkoti.capstone.mycommunity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewAnnouncementsListBinding;
import com.malkoti.capstone.mycommunity.databinding.ListItemAdBinding;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewAnnouncementsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewAnnouncementsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAnnouncementsList extends Fragment {
    private OnFragmentInteractionListener interactionListener;
    private FragmentViewAnnouncementsListBinding binding;
    private AnnouncementsListAdapter adapter;


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


    public ViewAnnouncementsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewAnnouncementsList.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAnnouncementsList newInstance() {
        ViewAnnouncementsList fragment = new ViewAnnouncementsList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_announcements_list,
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


    private void initUI() {
        binding.adsListRv.setHasFixedSize(true);
        binding.adsListRv.setLayoutManager(new LinearLayoutManager(ViewAnnouncementsList.this.getContext()));
        binding.adsListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new AnnouncementsListAdapter(() -> interactionListener.onFragmentInteraction(null));
        adapter.setData(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        binding.adsListRv.setAdapter(adapter);
    }

}


class AnnouncementsListAdapter extends RecyclerView.Adapter<AnnouncementsListAdapter.AdsViewHolder> {
    private List<Integer> ads;
    private OnAdsItemClickListener listener;

    /**
     *
     */
    interface OnAdsItemClickListener {
        void onItemClick();
    }


    public AnnouncementsListAdapter(OnAdsItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ListItemAdBinding binding = ListItemAdBinding.inflate(inflater, viewGroup, false);

        return new AdsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder viewHolder, int i) {
        viewHolder.bindView(ads.get(i));
    }

    @Override
    public int getItemCount() {
        return ads==null? 0: ads.size();
    }

    /**
     *
     * @param ads
     */
    public void setData(List<Integer> ads) {
        this.ads = ads;
        notifyDataSetChanged();
    }


    /**
     *
     */
    public class AdsViewHolder extends RecyclerView.ViewHolder {
        ListItemAdBinding itemBinding;

        public AdsViewHolder(ListItemAdBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        void bindView(int position) {
            itemBinding.adItemTitleTv.setText("Fitness center renovation");
            itemBinding.adItemByTv.setText("Management Management");
            itemBinding.adItemDateTv.setText("10-01-2018");

            itemBinding.adItemContainer.setOnClickListener(view -> listener.onItemClick());

            itemBinding.executePendingBindings();
        }
    }
}