package fayaj.maps.app.maps.models;

import com.google.gson.annotations.SerializedName;

public class Address{

	@SerializedName("country")
	private String country;

	@SerializedName("city")
	private String city;

	@SerializedName("district")
	private String district;

	@SerializedName("postalCode")
	private String postalCode;

	@SerializedName("county")
	private String county;

	@SerializedName("state")
	private String state;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}

	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}

	public String getPostalCode(){
		return postalCode;
	}

	public void setCounty(String county){
		this.county = county;
	}

	public String getCounty(){
		return county;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	@Override
 	public String toString(){
		return 
			"Address{" + 
			"country = '" + country + '\'' + 
			",city = '" + city + '\'' + 
			",district = '" + district + '\'' + 
			",postalCode = '" + postalCode + '\'' + 
			",county = '" + county + '\'' + 
			",state = '" + state + '\'' + 
			"}";
		}
}