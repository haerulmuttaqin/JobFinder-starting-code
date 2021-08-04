package id.haerulmuttaqin.jobfinder.ui;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import id.haerulmuttaqin.jobfinder.R;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.databinding.ItemJobMarkedBinding;

public class MainMarkedAdapter extends RecyclerView.Adapter<MainMarkedAdapter.RecyclerViewAdapter> {

    private List<GithubJob> data;
    private MainViewModel viewModel;

    protected MainMarkedAdapter(List<GithubJob> data, MainViewModel viewModel) {
        this.data = data;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemJobMarkedBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_job_marked, parent, false);
        return new RecyclerViewAdapter(binding);
    }

    public void clear() {
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {
        GithubJob items = data.get(position);
        if(items != null) {
            holder.bind(items, viewModel);
        }
    }

    @Override
    public int getItemCount() {
        return  data == null ? 0 : data.size();
    }

    public static class RecyclerViewAdapter extends RecyclerView.ViewHolder {
        ItemJobMarkedBinding binding;
        public RecyclerViewAdapter(ItemJobMarkedBinding binding) {
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
}