package id.haerulmuttaqin.jobfinder.data.paging;

/**
 * Created by rogergcc on 25/03/2021.
 * Copyright Ⓒ 2021 . All rights reserved.
 */
import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import id.haerulmuttaqin.jobfinder.data.storage.GithubJobDao;

public class LocalDataSourceFactory extends DataSource.Factory {

    private LocalPageKeyedDataSource localPageKeyedDataSource;

    public LocalDataSourceFactory(GithubJobDao dao, String keyword) {
        localPageKeyedDataSource = new LocalPageKeyedDataSource(dao, keyword);
    }

    @NonNull
    @Override
    public DataSource create() {
        return localPageKeyedDataSource;
    }
}