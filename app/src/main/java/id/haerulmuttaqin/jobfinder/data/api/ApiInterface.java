package id.haerulmuttaqin.jobfinder.data.api;

import java.util.List;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("positions.json")
    Call<List<GithubJob>> getJobList();

    @GET("positions.json")
    Call<List<GithubJob>> getJobList(@Query("page") int page);

    @GET("positions.json")
    Call<List<GithubJob>> searchJobList(@Query("search") String keyword);

    @GET("positions.json")
    Call<List<GithubJob>> searchJobList(@Query("page") int page, @Query("search") String keyword);
}