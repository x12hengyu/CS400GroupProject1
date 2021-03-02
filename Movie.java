// --== CS400 File Header Information ==--
// Name: Nott Laoaron
// Email: laoaron@wisc.edu
// Team: red
// Role: Data Wrangler
// TA: Xi Chen
// Lecturer: Florian Heimerl
// Notes to Grader: This is object for storing a movie's data

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the class that implements MovieInterface
 * It represents objects that contains information of movies
 * @author Nott Laoaron (Data Wrangler)
 */
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
	
    /**
     * The constructor of this class
     * @param data the list of data of a movie
     */
    Movie(ArrayList<String>[] data){
        this.data = data;
    }
	
    /**
     * The constructor of this class
     * @param data the list of data of a movie
     */
    @Override
	public String getTitle(){
        return data[0].get(0);
    }
	
    /**
     * Get the year of movie
     * @return a 4-digit integer of year of movie
     */
    @Override
	public Integer getYear(){
        return Integer.parseInt(data[2].get(0));
    }
	
    /**
     * Get the list of genres of this movie
     * @return the list of strings of genres of this movie
     */
    @Override
	public List<String> getGenres(){
        return data[3];
    }
	
    /**
     * Get the name of the director
     * @return the string of the name of the director
     */
    @Override
	public String getDirector(){
        return data[7].get(0);
    }
	
    /**
     * Get the description
     * @return the string of description of the movie
     */
    @Override
	public String getDescription(){
        String fullDescription = "";
        for(String cur: data[11 ]) {
          fullDescription += cur;
        }
        return fullDescription;
    }
	
    /**
     * Get the average vote
     * @return the real number between 0.0 and 10.0 (inclusive) representing the score of this movie
     */
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
