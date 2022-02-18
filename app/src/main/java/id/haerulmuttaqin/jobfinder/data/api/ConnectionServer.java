package id.haerulmuttaqin.jobfinder.data.api;

import java.util.List;

import javax.inject.Inject;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import retrofit2.Call;

public class ConnectionServer {

    @Inject
    ApiInterface apiInterface;

    public ConnectionServer(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public Call<List<GithubJob>> getJobList() {
        return apiInterface.getJobList();
    }

    public Call<List<GithubJob>> getJobListByPage(int page) {
        return apiInterface.getJobList(page);
    }

    public Call<List<GithubJob>> searchJobList(String keyword) {
        return apiInterface.searchJobList(keyword);
    }

    public Call<List<GithubJob>> searchJobListByPage(int page, String keyword) {
        return apiInterface.searchJobList(page, keyword);
    }
}
