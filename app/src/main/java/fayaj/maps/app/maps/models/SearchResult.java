package fayaj.maps.app.maps.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult{

	@SerializedName("suggestions")
	private List<SuggestionsItem> suggestions;

	public void setSuggestions(List<SuggestionsItem> suggestions){
		this.suggestions = suggestions;
	}

	public List<SuggestionsItem> getSuggestions(){
		return suggestions;
	}

	@Override
 	public String toString(){
		return 
			"SearchResult{" + 
			"suggestions = '" + suggestions + '\'' + 
			"}";
		}
}