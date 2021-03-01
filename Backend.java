//--== CS400 File Header Information ==--
//Name: Yuchuan Li
//Email: yli2298@wisc.edu
//Team: red
//Role: Backend Developer
//TA: Xi Ta
//Lecturer: Gray Dahl
//Notes to Grader: none
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.DataFormatException;
/**
 * This class implements BackendInterface
 * @author Yuchuan Li
 */
public class Backend implements BackendInterface{
	//private instance fields
	private StringReader stringReader;
	private Reader inputFileReader;
	private MovieDataReader data;
	private List<Movie> allMovies; //all of the movies extracted
	private List<String> allGenres; //all the genres mentioned in all movies
	private List<String> currentGenres; //current list of genres to be searched about
	private List<String> currentRatings; //current list of ratings to be searched about
	private List<Movie> resultingList; //this list stores all the movies that satisfy the genre 
													//and rating conditions
	
	//hashtable storing the movies, with its genre-rating (String) as key and movie (MovieInterface) as value
	private BackendHashTable<String,Movie> movieStorage;
	
	//load all the movies from the list to the hashtable
	/**
	 * A private helper method used to store all the movies from allMovies list to the hashTable
	 */
	private void loadMovie()
	{
		movieStorage = new BackendHashTable(100);
		//Iterate through the list allMovies
		for(int i = 0; i < allMovies.size(); i++)
		{
			//add all movies to the hash table
			movieStorage.put(allMovies.get(i).getTitle()
					+allMovies.get(i).getGenres().toString()
					+allMovies.get(i).getAvgVote().toString(), allMovies.get(i));
		}
	}
	//Constructor with two variables
	/**
	 * Two variable constructor of the Backend class
	 * @param inputFileReader, the String reader to read the given file
	 * @param data, the file to be read
	 * @throws FileNotFoundException if the file cannot be found
	 * @throws IOException
	 * @throws DataFormatException if the data has unacceptable input
	 */
	public Backend(String[] args) throws FileNotFoundException, IOException, DataFormatException
	{
		//set the data
		this.inputFileReader = inputFileReader;
		this.data = data;
		//extract the data to the allMovie list
		allMovies = data.readDataSet(inputFileReader);
		//Initialize lists
		allGenres = new ArrayList<>();
		currentGenres = new ArrayList<>();
		currentRatings = new ArrayList<>();
		resultingList =  new ArrayList<Movie>();
		//get all the genres from the allMovies list
		for(int i = 0; i < allMovies.size(); i++)
		{
			for(int j = 0; j < allMovies.get(i).getGenres().size();j++)
			{
				if(!allGenres.contains(allMovies.get(i).getGenres().get(j)))
				{
					allGenres.add(allMovies.get(i).getGenres().get(j));
				}
			}
		}
		//store all the movies from the list to the hashTable
		loadMovie();
	}
	//Constructor with one variables
	/**
	 * One variable constructor of the Backend class
	 * @param stringReader, the String reader to read the given file
	 * @throws FileNotFoundException if the file cannot be found
	 * @throws IOException
	 * @throws DataFormatException if the data has unacceptable input
	 */
	public Backend(Reader stringReader) throws FileNotFoundException, IOException, DataFormatException {
		//set the data		
		MovieDataReader data = new MovieDataReader(); 
		//extract the data to the allMovie list
		allMovies = data.readDataSet(stringReader);
		//Initialize lists
		allGenres = new ArrayList<>();
		currentGenres = new ArrayList<>();
		currentRatings = new ArrayList<>();
		resultingList =  new ArrayList<Movie>();
		//get all the genres from the allmovie list
		for(int i = 0; i < allMovies.size(); i++)
		{
			for(int j = 0; j < allMovies.get(i).getGenres().size();j++)
			{
				if(!allGenres.contains(allMovies.get(i).getGenres().get(j)))
				{
					allGenres.add(allMovies.get(i).getGenres().get(j));
				}
			}
		}
		//store all the movies from the list to the hashTable
		loadMovie();
	}
	/**
	 * This method adds one genre to the currentGenres list so that it can be considered
	 * as one searching condition
	 * @param genre to be added
	 */
	@Override
	public void addGenre(String genre) {
		//add one genre to the current genre list
		this.currentGenres.add(genre);
		resultingList = new ArrayList<Movie>();
	}
	/**
	 * This method adds one rating to the currentRating list so that it can be considered
	 * as one searching condition
	 * @param one rating range to be added
	 */
	@Override
	public void addAvgRating(String rating) {
		//add one AvgRating to the current rating list
		this.currentRatings.add(rating);
		//sort the list from the smallest to the largest
		Collections.sort(this.currentRatings);
	}
	/**
	 * This method removes one genre from the currentGenres list so that it can no longer be 
	 * considered as one searching condition
	 * @param the genre to be removed
	 */
	@Override
	public void removeGenre(String genre) {
		//remove the given genre
		this.currentGenres.remove(genre);
		resultingList = new ArrayList<Movie>();
	}
	/**
	 * This method removes one rating from the currentRatings list so that it can no longer be 
	 * considered as one searching condition
	 * @param the rating to be removed
	 */
	@Override
	public void removeAvgRating(String rating) {
		//remove the given rating range
		this.currentRatings.remove(rating);
	}
	/**
	 * This accessor method returns the whole list of genre to be searched
	 * @return currentGenres, the current list of genres
	 */
	@Override
	public List<String> getGenres() {
		return this.currentGenres;
	}
	/**
	 * This accessor method returns the whole list of rating to be searched
	 * @return currentRating, the current list of ratings
	 */
	@Override
	public List<String> getAvgRatings() {
		return this.currentRatings;
	}
	/**
	 * This method gets the number of movies that has been found with given
	 * conditions
	 * @return the number of movies
	 */
	@Override
	public int getNumberOfMovies() {
//		int count = 0;
//		for(int i = 0; i < this.resultingList.size(); i++)
//		{
//			if(this.resultingList.get(i)!=null)
//				count ++;
//		}
		this.getThreeMovies(0); //call the getThreeMovies to update the resultingList
		return this.resultingList.size();
	}
	/**
	 * This method gets all the genres from all movies
	 * @return the list allGenres
	 */
	@Override
	public List<String> getAllGenres() {
		return this.allGenres;
	}
	/**
	 * This method returns top 3 movies starting from given index
	 * @return a list of movies that meets the criteria ranking from the highest rating to the lowest
	 */
	@Override
	public List<Movie> getThreeMovies(int startingIndex) {	
		//initialize the list to be returned
		List<Movie> topThree = new ArrayList<>();
		//Case 1: genre is not selected
		if(this.currentGenres.isEmpty())
			return new ArrayList<Movie>();
		//Case 2: genre is selected while rating is not
		if(this.currentRatings.isEmpty())
		{
			//extract this list of movies that meet the requirements to resultingList
			this.resultingList = movieStorage.getTop(null, this.currentGenres);
			//add the top 1 at startingIndex of the resultingList to the topThree list
			if(startingIndex < resultingList.size())
				topThree.add(0,resultingList.get(startingIndex));
			//add the top 2 at startingIndex + 1 of the resultingList to the topThree list
			if(startingIndex + 1 < resultingList.size())
				topThree.add(1,resultingList.get(startingIndex + 1));
			//add the top 3 at startingIndex + 2 of the resultingList to the topThree list
			if(startingIndex + 2 < resultingList.size())
				topThree.add(2,resultingList.get(startingIndex + 2));
			return topThree;
		}
		//Case 3: both genre and rating are selected
		Float[] ratingList = new Float[this.currentRatings.size()];
		//convert the currentRating into a Float list called ratingList
		for(int j = ratingList.length-1; j >= 0; j--)
		{
			ratingList[j] = Float.parseFloat(this.currentRatings.get(j));
		}
		//extract this list of movies that meet the requirements to resultingList
		this.resultingList = this.movieStorage.getTop(ratingList, this.currentGenres);
		//add the top 1 at startingIndex of the resultingList to the topThree list
		if(startingIndex < resultingList.size() && resultingList.get(startingIndex)!=null)
			topThree.add(0,resultingList.get(startingIndex));
		//add the top 2 at startingIndex of the resultingList + 1 to the topThree list
		if(startingIndex + 1 < resultingList.size() && resultingList.get(startingIndex + 1)!=null)
			topThree.add(1,resultingList.get(startingIndex + 1));
		//add the top 3 at startingIndex of the resultingList + 2 to the topThree list
		if(startingIndex + 2 < resultingList.size() && resultingList.get(startingIndex + 2)!=null)
			topThree.add(2,resultingList.get(startingIndex + 2));
		return topThree;
	}

}
