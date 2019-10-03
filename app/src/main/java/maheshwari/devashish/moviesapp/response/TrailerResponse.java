package maheshwari.devashish.moviesapp.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import maheshwari.devashish.moviesapp.model.Trailer;

public class TrailerResponse {

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<Trailer> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<Trailer> results){
		this.results = results;
	}

	public List<Trailer> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"TrailerResponse{" +
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}