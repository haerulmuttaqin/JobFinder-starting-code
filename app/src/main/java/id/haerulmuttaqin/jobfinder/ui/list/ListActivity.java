package id.haerulmuttaqin.jobfinder.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import id.haerulmuttaqin.jobfinder.R;
import id.haerulmuttaqin.jobfinder.base.BaseActivity;
import id.haerulmuttaqin.jobfinder.data.api.ConnectionServer;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.storage.GithubJobRepository;
import id.haerulmuttaqin.jobfinder.databinding.ActivityListBinding;
import id.haerulmuttaqin.jobfinder.ui.MainAdapter;
import id.haerulmuttaqin.jobfinder.ui.MainViewModel;
import id.haerulmuttaqin.jobfinder.ui.detail.DetailActivity;

public class ListActivity extends BaseActivity<ActivityListBinding, MainViewModel> implements MainViewModel.Navigator {

    @Inject
    GithubJobRepository repository;
    @Inject
    ConnectionServer server;

    private ActivityListBinding binding;
    private MainViewModel viewModel;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    public MainViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(
                this,
                new MainViewModel.ModelFactory(this, server, repository)
        ).get(MainViewModel.class);
        viewModel.setNavigator(this);
        if (getIntent().getStringExtra("recommended") != null) {
            setupActionBar("Recommended Jobs");
            getData(null);
            binding.swipeRefresh.setOnRefreshListener(()->getData(null));
        }
        else if (getIntent().getStringExtra("search") != null) {
            String keyword = getIntent().getStringExtra("search");
            setupActionBar("Search Jobs (" + keyword + ")");
            getData(keyword);
            binding.swipeRefresh.setOnRefreshListener(()->getData(keyword));
        }
        else if (getIntent().getStringExtra("marked") != null) {
            setupActionBar("Marked Jobs");
            binding.swipeRefresh.setOnRefreshListener(null);
            viewModel.getLiveDataMarked().observe(this, githubJobs -> {
                hideProgress();
                if (githubJobs.size() > 0) {
                    binding.recyclerView.setAdapter(new MainAdapter(githubJobs, viewModel));
                } else {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.textEmptyErr.setText("No job marked!");
                }
            });
        }
        else {
            Toast.makeText(this, "Failed get list!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getData(String keyword) {
        final ListPagedAdapter adapter = new ListPagedAdapter(viewModel);
        viewModel.getPagedData(keyword).observe(this, githubJobs -> {
            adapter.submitList(githubJobs);
        });
        viewModel.getNetworkState().observe(this, networkState -> {
            adapter.setNetworkState(networkState);
            binding.setState(networkState);
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupActionBar(String title) {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        binding.swipeRefresh.setRefreshing(true);
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        binding.swipeRefresh.setRefreshing(false);
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.GONE);
        binding.shimmer.stopShimmer();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetResult(boolean status, String message) {
        if (!status) { //<-- status result is FALSE
            binding.textEmptyErr.setText(message);
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMark(int mark, String title) {
        Snackbar.make(binding.getRoot(), mark == 0 ? "\uD83D\uDE13 Unmark " + title : "\uD83D\uDE0D Marked " + title, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(GithubJob githubJob) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("item", githubJob);
        startActivity(intent);
    }
}