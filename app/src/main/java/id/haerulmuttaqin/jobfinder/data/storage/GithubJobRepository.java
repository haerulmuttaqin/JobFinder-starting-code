package id.haerulmuttaqin.jobfinder.data.storage;
import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;

public class GithubJobRepository {

    private static GithubJobDatabase database;
    private static GithubJobRepository repository;

    public GithubJobRepository(Context context) {
        database = GithubJobDatabase.getDatabase(context);
    }

    public static GithubJobRepository getInstance(Context context) {
        if (repository == null) {
            repository = new GithubJobRepository(context);
        }
        return repository;
    }

    public void updateMarkJob(GithubJob githubJob) {
        githubJob.is_mark = githubJob.is_mark == 1 ? 0 : 1;
        database.githubJobDao().insert(githubJob);
    }

    public void insert(GithubJob githubJob) {
        if (database.githubJobDao().getDataById(githubJob.id) == null) {
            database.githubJobDao().insert(githubJob);
        }
    }

    public GithubJob getById(String id) {
        return database.githubJobDao().getDataById(id);
    }

    public LiveData<List<GithubJob>> getLiveData() {
        return database.githubJobDao().getLiveData();
    }

    public LiveData<List<GithubJob>> searchLiveData(String keyword) {
        keyword = "%" + keyword + "%";
        return database.githubJobDao().searchLiveData(keyword);
    }

    public LiveData<List<GithubJob>> getLiveDataMarked() {
        return database.githubJobDao().getLiveDataMarked();
    }
}