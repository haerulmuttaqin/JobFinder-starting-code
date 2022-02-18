package id.haerulmuttaqin.jobfinder.data.storage;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import id.haerulmuttaqin.jobfinder.Utils;
import id.haerulmuttaqin.jobfinder.data.api.ConnectionServer;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.entity.NetworkState;
import id.haerulmuttaqin.jobfinder.data.paging.GithubDataSourceFactory;
import id.haerulmuttaqin.jobfinder.data.paging.GithubNetwork;
import id.haerulmuttaqin.jobfinder.data.paging.LocalDataSourceFactory;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
//import rx.schedulers.Schedulers;

public class GithubJobRepository {

    private static GithubJobDatabase database;
    private static GithubJobRepository repository;

    private static GithubNetwork network;
    private MediatorLiveData githubMediatorLiveData;
    private LiveData<PagedList<GithubJob>> pagedListLiveData;
    public String TAG = "GithubJobRepository";

    public GithubJobRepository(Context context) {
        database = GithubJobDatabase.getDatabase(context);
    }

    public static GithubJobRepository getInstance(Context context) {
        if (repository == null) {
            repository = new GithubJobRepository(context);
        }
        return repository;
    }

    //Insert Genre
    public void insert(final GithubJob githubJob) {
//        isLoading.setValue(true);

        //githubJob.is_mark = githubJob.is_mark == 1 ? 0 : 1;
        if (database.githubJobDao().getDataById(githubJob.id) != null) return;

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
//                genreDao.insert(githubJob);
                database.githubJobDao().insert(githubJob);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: Called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Called");
                        //isLoading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    public void updateMarkJob(GithubJob githubJob) {
        githubJob.is_mark = githubJob.is_mark == 1 ? 0 : 1;
        database.githubJobDao().insert(githubJob);
    }

//    public void insert(GithubJob githubJob) {
//        if (database.githubJobDao().getDataById(githubJob.id) == null) {
//            database.githubJobDao().insert(githubJob);
//        }
//    }

//    public GithubJob getById(String id) {
//        return database.githubJobDao().getDataById(id);
//    }

    public Flowable<GithubJob> getById(String  id){
        return database.githubJobDao().getDataById(id);
    }

//    public LiveData<List<GithubJob>> getLiveData() {
//        return database.githubJobDao().getLiveData();
//    }

    //Get all Genre
    public Flowable<List<GithubJob>> getLiveData(){
        return database.githubJobDao().getLiveData();
    }

    public LiveData<List<GithubJob>> searchLiveData(String keyword) {
        keyword = "%" + keyword + "%";
        return database.githubJobDao().searchLiveData(keyword);
    }

    public LiveData<List<GithubJob>> getLiveDataMarked() {
        return database.githubJobDao().getLiveDataMarked();
    }


    /*PAGING*/
    public LiveData<PagedList<GithubJob>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    private PagedList.BoundaryCallback<GithubJob> boundaryCallback = new PagedList.BoundaryCallback<GithubJob>() {
        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            githubMediatorLiveData.addSource(getPagedListLiveData(), value -> {
                githubMediatorLiveData.setValue(value);
                githubMediatorLiveData.removeSource(getPagedListLiveData());
            });
        }
    };

    public void initPageDao(String keyword) {
        PagedList.Config config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(Integer.MAX_VALUE).setPageSize(Integer.MAX_VALUE).build();
        Executor executor = Executors.newFixedThreadPool(3);
        LocalDataSourceFactory localDataSourceFactory = new LocalDataSourceFactory(database.githubJobDao(), keyword);
        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(localDataSourceFactory, config);
        pagedListLiveData = livePagedListBuilder.setFetchExecutor(executor).build();
    }

    public LiveData<PagedList<GithubJob>> getDataByPage(ConnectionServer connectionServer, GithubJobRepository repository, String keyword) {
        initPageDao(keyword);
        GithubDataSourceFactory githubDataSourceFactory = new GithubDataSourceFactory(connectionServer, repository, keyword);
        network = new GithubNetwork(githubDataSourceFactory, boundaryCallback);
        githubMediatorLiveData = new MediatorLiveData<>();
        githubMediatorLiveData.addSource(network.getPagedListLiveData(), value -> {
            githubMediatorLiveData.setValue(value);
        });
        githubDataSourceFactory.getData()
                .observeOn(Schedulers.io())
                .subscribe(item -> {
                    item.createdAt = Utils.dateFormatter(item.createdAt);
                    database.githubJobDao().insert(item);
                });
        return githubMediatorLiveData;
    }

    public LiveData<NetworkState> getNetworkState() {
        return network.getNetworkStateLiveData();
    }
}