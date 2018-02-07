package fayaj.maps.app.maps.search;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import fayaj.maps.app.maps.models.SearchResult;
import fayaj.maps.app.maps.service.ServiceManager;
import fayaj.maps.app.maps.utils.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by imran on 04/02/18.
 */

public class SearchLocationActivityPresenter implements SearchLocationActivityContract.Presenter {

    SearchLocationActivity searchLocationActivity;
    ServiceManager serviceManager;

    public SearchLocationActivityPresenter(SearchLocationActivity searchLocationActivity, ServiceManager serviceManager) {
        this.searchLocationActivity = searchLocationActivity;
        this.serviceManager = serviceManager;
    }

    @Override
    public void doSearch(String text) {
        Map<String, String> data = new HashMap<>();
        data.put("query", text);
        data.put("app_id", Constants.HEREMAPS_APPID);
        data.put("app_code", Constants.HEREMAPS_APPCODE);

        serviceManager.getSearchedResult(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::onError)
                .subscribe(this::onSuccess, this::onError);
    }


    private void onError(Throwable throwable) {
        searchLocationActivity.hideLoading();
        Log.e("t", throwable.getLocalizedMessage());
    }

    private void onSuccess(SearchResult searchResult) {
        searchLocationActivity.hideLoading();
        searchLocationActivity.getSearchResults(searchResult);
        Log.e("Suc", "suc");
    }
}
