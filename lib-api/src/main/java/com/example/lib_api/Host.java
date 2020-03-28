package com.example.lib_api;

import java.util.HashMap;
import java.util.Map;

public enum Host {
	GITHUB_API("api.github.com", "api.github.com", "api.github.com", "api.github.com"),
	TOSS_API("search-mock.herokuapp.com", "search-mock.herokuapp.com", "search-mock.herokuapp.com", "search-mock.herokuapp.com"),
	;

	private Map<ApiMode, String> hostAdressMap = new HashMap<ApiMode, String>();

	Host(String real, String stage, String alpha, String dev) {
		hostAdressMap.put(ApiMode.REAL, real);
		hostAdressMap.put(ApiMode.STAGE, stage);
		hostAdressMap.put(ApiMode.TEST, alpha);
		hostAdressMap.put(ApiMode.DEV, dev);
	}

	public String getDomain() {
		return hostAdressMap.get(ApiMode.REAL);
	}
}