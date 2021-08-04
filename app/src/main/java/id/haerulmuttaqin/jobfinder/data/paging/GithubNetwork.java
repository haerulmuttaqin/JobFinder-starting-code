package id.haerulmuttaqin.jobfinder.data.paging;

/**
 * Created by rogergcc on 25/03/2021.
 * Copyright Ⓒ 2021 . All rights reserved.
 */
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.entity.NetworkState;

public class GithubNetwork {

    final private LiveData<PagedList<GithubJob>> pagedListLiveData;
    final private LiveData<NetworkState> networkStateLiveData;

    public GithubNetwork(GithubDataSourceFactory githubDataSourceFactory, PagedList.BoundaryCallback<GithubJob> boundaryCallback) {
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(1)
                .setPageSize(50)
                .build();

        this.networkStateLiveData = Transformations.switchMap(
                githubDataSourceFactory.getNetworkStatus(),
                (Function<GithubPageKeyedDataSource, LiveData<NetworkState>>) GithubPageKeyedDataSource::getNetworkState);

        Executor executor = Executors.newFixedThreadPool(3);
        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(githubDataSourceFactory, config);
        this.pagedListLiveData = livePagedListBuilder.setFetchExecutor(executor).setBoundaryCallback(boundaryCallback).build();
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }

    public LiveData<PagedList<GithubJob>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}