package id.haerulmuttaqin.jobfinder.data.api;

import java.util.List;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("positions.json")
    Call<List<GithubJob>> getJobList();

}
