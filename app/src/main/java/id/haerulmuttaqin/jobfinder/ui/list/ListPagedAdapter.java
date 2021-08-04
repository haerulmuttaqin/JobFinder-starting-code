package id.haerulmuttaqin.jobfinder.ui.list;

/**
 * Created by rogergcc on 25/03/2021.
 * Copyright Ⓒ 2021 . All rights reserved.
 */
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import id.haerulmuttaqin.jobfinder.R;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.entity.NetworkState;
import id.haerulmuttaqin.jobfinder.databinding.ItemJobBinding;
import id.haerulmuttaqin.jobfinder.databinding.ItemJobLoadingBinding;
import id.haerulmuttaqin.jobfinder.ui.MainViewModel;

public class ListPagedAdapter extends PagedListAdapter<GithubJob, RecyclerView.ViewHolder> {

    private MainViewModel viewModel;
    private NetworkState networkState;

    public ListPagedAdapter(MainViewModel viewModel) {
        super(GithubJob.DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_job) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemJobBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_job, parent, false);
            return new RecyclerViewAdapter(binding);
        }
        else if (viewType == R.layout.item_job_loading) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemJobLoadingBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_job_loading, parent, false);
            return new LoadingViewAdapter(binding);
        }
        else {
            throw  new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_job:
                GithubJob item = getItem(position);
                if (item != null) {
                    ((RecyclerViewAdapter) holder).bind(item, viewModel);
                }
                break;
            case R.layout.item_job_loading:
                ((LoadingViewAdapter) holder).bind(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_job_loading;
        } else {
            return R.layout.item_job;
        }
    }

    public void setNetworkState(NetworkState networkState) {
        this.networkState = networkState;
        boolean previousExtraRow = hasExtraRow();
        NetworkState previousState = this.networkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            }
            else {
                notifyItemInserted(getItemCount());
            }
        }
        else if (newExtraRow && previousState != networkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    public static class RecyclerViewAdapter extends RecyclerView.ViewHolder {
        ItemJobBinding binding;
        public RecyclerViewAdapter(ItemJobBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        void bind(@NonNull GithubJob data, MainViewModel viewModel) {
            Glide.with(binding.photoPreview.getContext())
                    .load(data.companyLogo)
                    .error(binding.photoPreview.getContext().getDrawable(R.drawable.ic_round_business_center_24))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.progress.stopShimmer();
                            binding.progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.progress.stopShimmer();
                            binding.progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(binding.photoPreview);
            binding.setItem(data);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public static class LoadingViewAdapter extends RecyclerView.ViewHolder {
        ItemJobLoadingBinding binding;
        public LoadingViewAdapter(ItemJobLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        void bind(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                binding.errorMsg.setVisibility(View.VISIBLE);
                binding.errorMsg.setText(networkState.getMsg());
            } else {
                binding.errorMsg.setVisibility(View.GONE);
            }

            binding.executePendingBindings();
        }
    }
}