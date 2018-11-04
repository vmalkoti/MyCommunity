package com.malkoti.capstone.mycommunity;

import android.arch.lifecycle.Observer;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malkoti.capstone.mycommunity.databinding.FragmentViewAnnouncementsListBinding;
import com.malkoti.capstone.mycommunity.databinding.ListItemAdBinding;
import com.malkoti.capstone.mycommunity.model.AnnouncementPost;
import com.malkoti.capstone.mycommunity.viewmodels.MainViewModel;

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
    public ViewAnnouncementsList() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewAnnouncementsList.
     */
    public static ViewAnnouncementsList newInstance() {
        ViewAnnouncementsList fragment = new ViewAnnouncementsList();
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

    /**
     * Initialize UI components
     */
    private void initUI() {
        adapter = new AnnouncementsListAdapter(() -> interactionListener.onFragmentInteraction(null));

        viewModel.getAnnouncements().observe(getActivity(), posts -> adapter.setData(posts));

        binding.adsListRv.setHasFixedSize(true);
        binding.adsListRv.setLayoutManager(new LinearLayoutManager(ViewAnnouncementsList.this.getContext()));
        binding.adsListRv.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.adsListRv.setAdapter(adapter);
    }
}


/**
 * Adapter class for announcement list recyclerview
 */
class AnnouncementsListAdapter extends RecyclerView.Adapter<AnnouncementsListAdapter.AdsViewHolder> {
    private static final String LOG_TAG = "DEBUG_" + AnnouncementsListAdapter.class.getSimpleName();

    private List<AnnouncementPost> announcements;
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
        viewHolder.bindView(announcements.get(i));
    }

    @Override
    public int getItemCount() {
        return announcements == null ? 0 : announcements.size();
    }

    /**
     * @param posts
     */
    public void setData(List<AnnouncementPost> posts) {
        Log.d(LOG_TAG, "setData: New data received: " + (posts==null ? -1: posts.size()));

        this.announcements = posts;
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

        void bindView(AnnouncementPost post) {
            itemBinding.adItemTitleTv.setText(post.announcementTitle);
            //itemBinding.adItemByTv.setText("Management Management");
            itemBinding.adItemDateTv.setText(post.postDate);

            itemBinding.adItemContainer.setOnClickListener(view -> listener.onItemClick());

            itemBinding.executePendingBindings();
        }
    }
}