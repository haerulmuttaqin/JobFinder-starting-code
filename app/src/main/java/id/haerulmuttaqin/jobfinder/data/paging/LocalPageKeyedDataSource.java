package id.haerulmuttaqin.jobfinder.data.paging;

/**
 * Created by rogergcc on 25/03/2021.
 * Copyright Ⓒ 2021 . All rights reserved.
 */
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.List;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import id.haerulmuttaqin.jobfinder.data.storage.GithubJobDao;

public class LocalPageKeyedDataSource extends PageKeyedDataSource<String, GithubJob> {

    private final GithubJobDao dao;
    private String keyword;

    public LocalPageKeyedDataSource(GithubJobDao dao, String keyword) {
        this.dao = dao;
        this.keyword = keyword;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, GithubJob> callback) {
        List<GithubJob> data = null;
        if (keyword == null) {
            data = dao.getList();
        } else {
            data = dao.searchList(keyword);
        }

        if (data.size() != 0) {
            callback.onResult(data, Integer.toString(0), Integer.toString(1));
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, GithubJob> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, GithubJob> callback) {

    }
}