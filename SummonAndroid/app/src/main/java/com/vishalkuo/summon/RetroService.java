package com.vishalkuo.summon;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by vishalkuo on 15-07-01.
 */
public interface RetroService {
    @POST("/api/v1/tableRequest")
    void newPostTask(@Body TableRequest tableRequest, Callback<String> taskCallback);

}
