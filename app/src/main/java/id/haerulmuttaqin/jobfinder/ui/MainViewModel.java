package id.haerulmuttaqin.jobfinder.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import java.util.List;

import id.haerulmuttaqin.jobfinder.Utils;
import id.haerulmuttaqin.jobfinder.base.BaseViewModel;
import id.haerulmuttaqin.jobfinder.data.api.ConnectionServer;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.entity.NetworkState;
import id.haerulmuttaqin.jobfinder.data.storage.GithubJobRepository;
import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseViewModel<MainViewModel.Navigator> {

    public MainViewModel(Context context, ConnectionServer connectionServer, GithubJobRepository repository) {
        super(context, connectionServer, repository);
    }

    public LiveData<PagedList<GithubJob>> getPagedData(String keyword) {
        return getRepository().getDataByPage(getConnectionServer(), getRepository(), keyword);
    }

    public LiveData<NetworkState> getNetworkState() {
        return getRepository().getNetworkState();
    }

//    public LiveData<List<GithubJob>> getLiveData() {
//        return getRepository().getLiveData();
//    }

    public Flowable<List<GithubJob>> getLiveData(){
        return getRepository().getLiveData();
    }

    public LiveData<List<GithubJob>> searchLiveData(String keyword) {
        return getRepository().searchLiveData(keyword.toLowerCase());
    }

    public LiveData<List<GithubJob>> getLiveDataMarked() {
        return getRepository().getLiveDataMarked();
    }

    public String formatDate(String date){
        return Utils.dateToTimeFormat(date);
    }

    public void getJobFromServer() {
        getNavigator().showProgress();
        getConnectionServer().getJobList().enqueue(new Callback<List<GithubJob>>() {
            @Override
            public void onResponse(Call<List<GithubJob>> call, Response<List<GithubJob>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    for (GithubJob item : response.body()) {
                        item.createdAt = Utils.dateFormatter(item.createdAt); // format date
                        getRepository().insert(item);
                    }
                    getNavigator().onGetResult(true, "Success");
                }
                getNavigator().hideProgress();
            }
            @Override
            public void onFailure(Call<List<GithubJob>> call, Throwable t) {
                t.getLocalizedMessage();
                getNavigator().hideProgress();
                getNavigator().onGetResult(false, Utils.errorMessageHandler(call, t));
            }
        });
    }

    public void searchJobFromServer(String keyword) {
        getNavigator().showProgress();
        getConnectionServer().searchJobList(keyword).enqueue(new Callback<List<GithubJob>>() {
            @Override
            public void onResponse(Call<List<GithubJob>> call, Response<List<GithubJob>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    for (GithubJob item : response.body()) {
                        getRepository().insert(item);
                    }
                    getNavigator().onGetResult(true, "Success");
                }
                getNavigator().hideProgress();
            }
            @Override
            public void onFailure(Call<List<GithubJob>> call, Throwable t) {
                t.getLocalizedMessage();
                getNavigator().hideProgress();
                getNavigator().onGetResult(false, Utils.errorMessageHandler(call, t));
            }
        });
    }

    public void markJob(GithubJob githubJob) {
        getRepository().updateMarkJob(githubJob);
        getNavigator().onMark(githubJob.is_mark, githubJob.title);
    }

    public void itemClick(GithubJob githubJob) {
        getNavigator().onItemClick(githubJob);
    }

    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer server;
        private GithubJobRepository repository;
        public ModelFactory(Context context, ConnectionServer server, GithubJobRepository repository) {
            this.context = context;
            this.server = server;
            this.repository = repository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainViewModel(context, server, repository);
        }
    }

    public interface Navigator {
        void showProgress();
        void hideProgress();
        void onGetResult(boolean status, String message);
        void onMark(int mark, String title);
        void onItemClick(GithubJob githubJob);
    }
}