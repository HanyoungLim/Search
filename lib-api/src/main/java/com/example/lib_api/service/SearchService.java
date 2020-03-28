package com.example.lib_api.service;

import com.example.lib_api.Host;
import com.example.lib_api.Scheme;
import com.example.lib_api.model.Users;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchService {

    Scheme scheme = Scheme.HTTPS;
    Host host = Host.TOSS_API;

    @GET("/search")
    Single<Users> getUsersByKeyword(@Query("keyword") String keyword);
}
