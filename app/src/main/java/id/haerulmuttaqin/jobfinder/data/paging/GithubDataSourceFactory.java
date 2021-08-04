package id.haerulmuttaqin.jobfinder.data.paging;

/**
 * Created by rogergcc on 25/03/2021.
 * Copyright Ⓒ 2021 . All rights reserved.
 */


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import id.haerulmuttaqin.jobfinder.data.api.ConnectionServer;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.storage.GithubJobRepository;
import io.reactivex.subjects.ReplaySubject;
//import rx.subjects.ReplaySubject;

public class GithubDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<GithubPageKeyedDataSource> networkStatus;
    private GithubPageKeyedDataSource githubPageKeyedDataSource;
    public GithubDataSourceFactory(ConnectionServer connectionServer, GithubJobRepository repository, String keyword) {
        this.networkStatus = new MutableLiveData<>();
        githubPageKeyedDataSource = new GithubPageKeyedDataSource(connectionServer, repository, keyword);
    }

    @NonNull
    @Override
    public DataSource create() {
        networkStatus.postValue(githubPageKeyedDataSource);
        return githubPageKeyedDataSource;
    }

    public MutableLiveData<GithubPageKeyedDataSource> getNetworkStatus() {
        return networkStatus;
    }

    public ReplaySubject<GithubJob> getData() {
        return githubPageKeyedDataSource.getGithubJobObserve();
    }
}
