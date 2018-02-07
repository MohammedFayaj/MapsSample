package fayaj.maps.app.maps.search;

import fayaj.maps.app.maps.models.SearchResult;

/**
 * Created by imran on 04/02/18.
 */

public interface SearchLocationActivityContract {

    interface Presenter {
        void doSearch(String text);
    }

    interface View {
        void getSearchResults(SearchResult searchResult);
        void showLoading();
        void hideLoading();
    }
}
