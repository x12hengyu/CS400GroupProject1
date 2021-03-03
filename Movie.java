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
    private ArrayList<String> genre = new ArrayList<String>();
    private String description, title, director;
    private int year;
    private Float avg_vote;
	
    /**
     * The constructor of this class
     * @param data the list of data of a movie
     */
    Movie(String[] data){
        // 0 = title
        // 1 = year
        // 2 = genre
        // 3 = director
        // 4 = description
        // 5 = avg_vote
        title = data[0];
        year = Integer.parseInt(data[1]);
        String[] tmp = data[2].replaceAll("\"", "").split(", ");
        for(String it : tmp) genre.add(it);
        director = data[3].replaceAll("\"","");
        description = data[4];
        avg_vote = Float.parseFloat(data[5]);
    }
	
    /**
     * The constructor of this class
     * @param data the list of data of a movie
     */
    @Override
	public String getTitle(){
        return title;
    }
	
    /**
     * Get the year of movie
     * @return a 4-digit integer of year of movie
     */
    @Override
	public Integer getYear(){
        return year;
    }
	
    /**
     * Get the list of genres of this movie
     * @return the list of strings of genres of this movie
     */
    @Override
	public List<String> getGenres(){
        return genre;
    }
	
    /**
     * Get the name of the director
     * @return the string of the name of the director
     */
    @Override
	public String getDirector(){
        return director;
    }
	
    /**
     * Get the description
     * @return the string of description of the movie
     */
    @Override
	public String getDescription(){
        return description;
    }
	
    /**
     * Get the average vote
     * @return the real number between 0.0 and 10.0 (inclusive) representing the score of this movie
     */
    @Override
	public Float getAvgVote(){
        return avg_vote;
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
