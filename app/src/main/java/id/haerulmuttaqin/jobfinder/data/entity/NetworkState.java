package id.haerulmuttaqin.jobfinder.data.entity;

/**
 * Created by rogergcc on 25/03/2021.
 * Copyright Ⓒ 2021 . All rights reserved.
 */
public class NetworkState {

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public enum Status {
        FIRST_LOADING,
        FIRST_FAILED,
        RUNNING,
        SUCCESS,
        EMPTY,
        FAILED
    }

    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;
    public static final NetworkState FIRST_LOADING;
    public static final NetworkState FIRST_FAILED;
    public static final NetworkState EMPTY;

    static {
        LOADED = new NetworkState(Status.SUCCESS,"Success");
        LOADING = new NetworkState(Status.RUNNING,"Running");
        FIRST_LOADING = new NetworkState(Status.FIRST_LOADING,"First Runnig");
        FIRST_FAILED = new NetworkState(Status.FIRST_FAILED,"First Failed");
        EMPTY = new NetworkState(Status.EMPTY, "Empty");
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}