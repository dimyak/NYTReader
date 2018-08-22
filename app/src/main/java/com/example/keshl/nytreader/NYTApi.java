package com.example.keshl.nytreader;

import com.example.keshl.nytreader.model.ResponceList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NYTApi {

    @GET("mostviewed/{sections}/{days}.json")
    Call<ResponceList> getMostViewedDate(@Path("sections") String sections, @Path("days") String days,
                                         @Query("api-key") String apiKey);

    @GET("mostshared/{sections}/{days}.json")
    Call<ResponceList> getMostSharedDate(@Path("sections") String sections, @Path("days") String days,
                                         @Query("api-key") String apiKey);

    @GET("mostemailed/{sections}/{days}.json")
    Call<ResponceList> getMostEmailedDate(@Path("sections") String sections, @Path("days") String days,
                                          @Query("api-key") String apiKey);

}
