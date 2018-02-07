package fayaj.maps.app.maps.service;

import java.util.Map;

import fayaj.maps.app.maps.models.SearchResult;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by imran on 04/02/18.
 */

public interface ServiceManager {

    @GET("/suggest.json")
    Single<SearchResult> getSearchedResult(@QueryMap Map<String,String> options);
}
