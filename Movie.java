import java.util.ArrayList;
import java.util.List;

public class Movie implements MovieInterface {
    // 0 = title
    // 1 = original_title
    // 2 = year
    // 3 = genre
    // 4 = duration
    // 5 = country
    // 6 = language
    // 7 = director
    // 8 = writer
    // 9 = production_company
    // 10 = actors
    // 11 = description
    // 12 = avg_vote
    private ArrayList<String>[] data;
    Movie(ArrayList<String>[] data){
        this.data = data;
    }
    @Override
	public String getTitle(){
        return data[0].get(0);
    }
    @Override
	public Integer getYear(){
        return Integer.parseInt(data[2].get(0));
    }
    @Override
	public List<String> getGenres(){
        return data[3];
    }
    @Override
	public String getDirector(){
        return data[7].get(0);
    }
    @Override
	public String getDescription(){
        String fullDescription = "";
        for(String cur: data[11 ]) {
          fullDescription += cur;
        }
        return fullDescription;
    }
    @Override
	public Float getAvgVote(){
        return Float.parseFloat(data[12].get(0));
    }
	
	// from super interface Comparable
    @Override
	public int compareTo(Movie otherMovie){
        Float a = getAvgVote();
        Float b = otherMovie.getAvgVote();
        if(a > b) return -1;
        if(a < b) return 1;
        return 0;
    }
}
