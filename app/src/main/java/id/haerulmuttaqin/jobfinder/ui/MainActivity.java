package id.haerulmuttaqin.jobfinder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.haerul.bottomfluxdialog.BottomFluxDialog;

import java.util.List;

import javax.inject.Inject;

import id.haerulmuttaqin.jobfinder.R;
import id.haerulmuttaqin.jobfinder.base.BaseActivity;
import id.haerulmuttaqin.jobfinder.data.api.ConnectionServer;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.storage.GithubJobRepository;
import id.haerulmuttaqin.jobfinder.databinding.ActivityMainBinding;
import id.haerulmuttaqin.jobfinder.ui.detail.DetailActivity;
import id.haerulmuttaqin.jobfinder.ui.list.ListActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements  MainViewModel.Navigator {

    @Inject ConnectionServer server;
    @Inject GithubJobRepository repository;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this, new MainViewModel.ModelFactory(this, server, repository)).get(MainViewModel.class);
        viewModel.setNavigator(this);
        viewModel.getJobFromServer();
//        viewModel.getLiveData().observe(this, githubJobs -> {
//            binding.recyclerView.setAdapter(new MainAdapter(githubJobs, viewModel));
//        });

        Disposable disposable = viewModel.getLiveData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GithubJob>>() {
                    @Override
                    public void accept(List<GithubJob> githubJobs) throws Exception {
                        Log.d(TAG, "accept: getAllMovies");
                        binding.recyclerView.setAdapter(new MainAdapter(githubJobs, viewModel));
                    }
                });

        compositeDisposable.add(disposable);

        viewModel.getLiveDataMarked().observe(this, githubJobs -> {
            if (githubJobs.size() > 0) {
                binding.markedTitle.setVisibility(View.VISIBLE);
                binding.recyclerViewMarked.setVisibility(View.VISIBLE);
                binding.recyclerViewMarked.setAdapter(new MainMarkedAdapter(githubJobs, viewModel));
            } else {
                binding.markedTitle.setVisibility(View.GONE);
                binding.recyclerViewMarked.setVisibility(View.GONE);
            }
        });
        binding.swipeRefresh.setOnRefreshListener(()->viewModel.getJobFromServer());
        binding.cardSearch.setOnClickListener(v->{
            showDialogSearch();
        });
        binding.showAllMarked.setOnClickListener(v-> {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("marked", "marked");
            startActivity(intent);
        });
        binding.showAllRecommended.setOnClickListener(v-> {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("recommended", "recommended");
            startActivity(intent);
        });
    }


    private void showDialogSearch() {
        BottomFluxDialog.inputDialog(MainActivity.this)
                .setTextTitle("Search")
                .setTextMessage("What are you looking for?")
                .setRightButtonText("SUBMIT")
                .setInputListener(new BottomFluxDialog.OnInputListener() {
                    @Override
                    public void onSubmitInput(String text) {
                        Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        intent.putExtra("search", text);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelInput() {

                    }
                })
                .show();
//        View view = getLayoutInflater().inflate(R.layout.dialog_search, null);
//        TextInputEditText m = view.findViewById(R.id.message);
//        RelativeLayout btnSearch = view.findViewById(R.id.search);
//
//        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogStyle);
//        btnSearch.setOnClickListener(v -> {
//            Intent intent = new Intent(this, ListActivity.class);
//            intent.putExtra("search", m.getText().toString());
//            startActivity(intent);
//        });
//        m.requestFocus();
//
//        dialog.setContentView(view);
//        dialog.show();
    }

    @Override
    public void showProgress() {
        binding.swipeRefresh.setRefreshing(true);
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        binding.swipeRefresh.setRefreshing(false);
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.GONE);
        binding.shimmer.stopShimmer();
        binding.contentLayout.setVisibility(View.VISIBLE);
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