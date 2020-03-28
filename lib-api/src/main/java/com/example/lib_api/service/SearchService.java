package com.example.lib_api.service;

import com.example.lib_api.Host;
import com.example.lib_api.Scheme;
import com.example.lib_api.model.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface SearchService {

    Scheme scheme = Scheme.HTTPS;
    Host host = Host.GITHUB_API;

    @GET("/users")
    Single<List<User>> getUser();
}
