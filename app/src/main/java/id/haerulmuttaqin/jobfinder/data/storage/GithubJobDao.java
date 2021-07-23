package id.haerulmuttaqin.jobfinder.data.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import io.reactivex.Flowable;

@Dao
public interface GithubJobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GithubJob githubJob);

    @Query("SELECT * FROM GithubJob ORDER BY createdAt DESC LIMIT 10")
//    LiveData<List<GithubJob>> getLiveData();
    Flowable<List<GithubJob>> getLiveData();

    @Query("SELECT * FROM githubjob ORDER BY createdAt DESC")
    List<GithubJob> getList();

    @Query("SELECT * FROM githubjob WHERE LOWER(title) LIKE :keyword OR LOWER(description) LIKE :keyword ORDER BY createdAt DESC")
    LiveData<List<GithubJob>> searchLiveData(String keyword);

    @Query("SELECT * FROM githubjob WHERE LOWER(title) LIKE :keyword OR LOWER(description) LIKE :keyword ORDER BY createdAt DESC")
    List<GithubJob> searchList(String keyword);

    @Query("SELECT * FROM githubjob where is_mark = 1 ORDER BY createdAt DESC")
    LiveData<List<GithubJob>> getLiveDataMarked();

    @Query("SELECT * FROM githubjob where id = :id")
//    GithubJob getDataById(String id);
    Flowable<GithubJob> getDataById(String id);

}