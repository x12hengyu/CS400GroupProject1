//--== CS400 File Header Information ==--
//Name: Yuchuan Li
//Email: yli2298@wisc.edu
//Team: red
//Role: Backend Developer
//TA: Xi Ta
//Lecturer: Gray Dahl
//Notes to Grader: none
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * This class models a node storing one key-value pair for HashTableMap 
 *
 * @param <KeyType, ValueType> the types of key and value respectively
 * @author Yuchuan Li
 */
class HashNode<KeyType, ValueType> { 
	//instance field storing key and value
	KeyType key; 
	ValueType value; 

	//Reference to previous and next node 
	HashNode<KeyType, ValueType> next; 
	HashNode<KeyType, ValueType> prev;

 	//Constructor 
	/**
	 * Construct one node with two parameter key and value
	 * @param key
	 * @param value
	 */
 	public HashNode(KeyType key, ValueType value) 
 	{ 
     this.key = key; 
     this.value = value; 
 	} 
} 
/**
 * This class models a generic HashTableMap 
 *
 * @param <KeyType, ValueType> the types of key and value respectively
 * @author Yuchuan Li
 */
public class BackendHashTable<KeyType, ValueType> implements MapADT<KeyType, ValueType>{
	//private instance field
	private LinkedList<HashNode<KeyType, ValueType>>[] hashPairs;
	private int TABLE_CAPACITY;
	private int size;
	
	//Constructor with parameter
	/**
	 * Construct one HashTableMap with one parameter
	 * @param capacity, the designated Table capacity for this HashTableMap
	 */
	public BackendHashTable(int capacity)
	{
		this.TABLE_CAPACITY = capacity;
		this.size = 0;
		hashPairs = (LinkedList<HashNode<KeyType, ValueType>>[]) new LinkedList [capacity];	
	}
	//private helper
	/**
	 * A private helper method to expand the capacity by doubling and rehash the original elements
	 * when load factor equals or greater than 95%
	 */
	private void capacityHelper()
	{
		//double the capacity
		TABLE_CAPACITY = TABLE_CAPACITY * 2;
		LinkedList<HashNode<KeyType, ValueType>>[] oldList = this.hashPairs;
		this.hashPairs = (LinkedList<HashNode<KeyType, ValueType>>[]) new LinkedList[TABLE_CAPACITY];	
		//rehashing
		this.size = 0;	//initialize the size
		//iterate through the original array to rehash
		for(int i = 0; i < oldList.length; i++)
		{
			if(oldList[i] != null)
			{
				//iterate through the collision linked list to rehash every pair
				for(int j = 0; j < oldList[i].size(); j++)
				{
					KeyType key = oldList[i].get(j).key;
					ValueType value = oldList[i].get(j).value;
					this.put(key, value);
				}
			}
		}
	}
	/**
	 * This method will add a key-value pair to the HashTableMap
	 * @param key, the given key
	 * @param value, the value 
	 * @return true when the pair is successfully added
	 * @return false if the key is null or already exists
	 */
	@Override
	public boolean put(KeyType key, ValueType movie) {
		//check if the key is null
		if(key == null)
			return false;
		//check if the key already exists
		if(this.containsKey(key))
			return false;
		//get the HashNode and the corresponding position index
		HashNode<KeyType, ValueType> putNode = new HashNode<>(key, movie);
		if(key instanceof String)
		{
			//hash the key and get the correct index
			int putIndex = Math.abs(key.hashCode()%this.TABLE_CAPACITY);
			LinkedList<HashNode<KeyType, ValueType>> putList = this.hashPairs[putIndex];
			//case 1: no hash collision yet
			if(putList==null || putList.isEmpty())
			{
				//initialize the collision list and add the first element
				this.hashPairs[putIndex] = new LinkedList<HashNode<KeyType, ValueType>>();
				putList = this.hashPairs[putIndex];
				//set the first and only pair
				putList.addFirst(putNode);
				putList.getFirst().next = null;
				putList.getFirst().prev = null;
				//update the size
				size++;
			}
			//case 2: hash collision happens
			else
			{
				//add the Node to the first position of the list
				putNode.prev = null;
				putNode.next = putList.getFirst();
				putList.getFirst().prev = putNode;
				putList.addFirst(putNode);
				//update the size
				size++;
			}
			//check if load capacity overflows
			if((double)this.size/this.TABLE_CAPACITY >= 0.95)
			{
				this.capacityHelper();
			}
			return true;
		}
		return false;
	}
	/**
	 * The accessor method that gives the value when given a key
	 * @param key, the key to get
	 * @return the value of the pair with given key
	 * @throws NoSuchElementException if the pair with given key cannot be found
	 */
	@Override
	public ValueType get(KeyType key) throws NoSuchElementException {
		//get the corresponding index
		int hash = Math.abs(key.hashCode()%this.TABLE_CAPACITY);
		//cannot find if the corresponding list is empty
		if(this.hashPairs[hash]==null || this.hashPairs[hash].size()==0)
			throw new NoSuchElementException("There is no such value with the key");
		HashNode<KeyType, ValueType> getNode = this.hashPairs[hash].getFirst();
		//iterate through the linked list to find the designated value from the key-value pair
		while(getNode != null)
		{
			if(getNode.key.equals(key))
				return getNode.value;
			getNode = getNode.next;
		}
		//if the key does not exist, throw exception
		throw new NoSuchElementException("There is no such value with the key");
	}
	
	/**
	 * This method is customized for Backend by return a list of movies that satisfy the genreList
	 * from the highest rating to the lowest according to the key list
	 * @param keyList
	 * @param genreList
	 * @return a list of movies that satisfy the conditions, ranking from the highest to lowest ratings
	 */
	public List<ValueType> getTop(Float[] ratingList, List<String> genreList){
		//initialize the list to be returned
		List<ValueType> list = new ArrayList<>();
		//Case 1: if the rating list is null, return all movies with designated genre from highest rating to lowest
		if(ratingList == null || ratingList.length == 0)
		{
			//iterate through the hash table
			for(int j = 0; j <this.hashPairs.length; j++)
			{
				if(hashPairs[j]!=null)
				{
					for(int k = 0; k < this.hashPairs[j].size(); k++)
					{
						//if the movie contains the given genre conditions
						if(((Movie)this.hashPairs[j].get(k).value).getGenres().containsAll(genreList))
						{
							//add this movie to list
							list.add((ValueType)this.hashPairs[j].get(k).value);
						}
					}
				}
			}
			//sort the list from the highest rating to the lowest
			selectionSort(list);
			return list;
		}
		//Case 2: if the rating list is not null, return all movies with designated genre from highest 
		// rating to lowest within the rating range indicated by the rating list

		//iterate through the hash table
		for(int j = 0; j <this.hashPairs.length; j++)
		{
			if(hashPairs[j]!=null)
			{
				for(int k = 0; k < this.hashPairs[j].size(); k++)
				{
					//if the movie contains the given genre conditions
					if(((Movie)this.hashPairs[j].get(k).value).getGenres().containsAll(genreList))
					{
						//iterate through the rating list from highest to lowest to check of the movie also fits
						// the rating conditions
						for(int i = ratingList.length-1; i >= 0; i--)
						{
							//check if the movie satisfies any of the rating conditions
							if(((Movie)this.hashPairs[j].get(k).value).getAvgVote() >= ratingList[i]
									&& ((Movie)this.hashPairs[j].get(k).value).getAvgVote() < (ratingList[i] + 1))
							{
								//if the movie satisfies both genre and rating conditions
								//	add this movie to list
								list.add((ValueType)this.hashPairs[j].get(k).value);
							}
						}
					}
				}
			}
		}
		//sort the list from the highest rating to the lowest
		selectionSort(list);
		return list;
	}
	/**
	 * A private helper method that helps sort the list from highest rating to the lowest
	 * @param list, the list to be sorted
	 */
	private void selectionSort(List<ValueType> list) {
	    for (int i = 0; i < list.size(); i++) {
	    	//find the max value in the rest of the list
	    	ValueType max =  list.get(i);
	        int maxId = i;
	        for (int j = i+1; j < list.size(); j++) {
	        	//check the rating (average vote) of two and get maximum value and its index
	            if (((Movie)list.get(j)).getAvgVote() > ((Movie) max).getAvgVote()) {
	                max = list.get(j);
	                maxId = j;
	            }
	        }
	        // swapping
	        ValueType temp = list.get(i);
	        list.set(i, max);
	        list.set(maxId, temp);
	    }
	}
	/**
	 * This method gives the number of pairs storing in the map
	 * @return size, the amount of pairs being stored in the HashTableMap
	 */
	@Override
	public int size() {
		return this.size;
	}
	/**
	 * This method checks if the given key exists in one of the pairs stored in the map
	 * @param key, the key given to find
	 * @return true if the pair with this key exist
	 * @return false if it does not exist
	 */
	@Override
	public boolean containsKey(KeyType key) {
		try {
			//check if the key exist
			this.get(key);
		}
		catch(NoSuchElementException e1){
			//if it does not exist, exception can be caught
			return false;
		}
		//if no exception being thrown, the key exist
		return true;
	}
	/**
	 * This method removes the pair with given key from the HashTableMap
	 * @param key, the key of the pair to be removed
	 * @return the value of the removed pair
	 * @return null if the pair does not exist
	 */
	@Override
	public ValueType remove(KeyType key) {
		//get the index used to remove
		int removeIndex = Math.abs(key.hashCode()%this.TABLE_CAPACITY);
		LinkedList<HashNode<KeyType, ValueType>> removeList = this.hashPairs[removeIndex];
		//check if the pair going to be removed exist
		if(removeList == null || removeList.size()==0)
			return null;
		HashNode<KeyType, ValueType> removeNode = this.hashPairs[removeIndex].getFirst();
		//iterate through the linked list to find the designated key-value pair and remove it
		while(removeNode != null)
		{
			//find the node
			if(removeNode.key.equals(key))
			{
				//Situation 1: in the middle of the collision list
				if(removeNode.prev != null && removeNode.next != null)
				{
					removeNode.prev.next = removeNode.next;
					removeNode.next.prev = removeNode.prev;
					removeList.remove(removeNode);
					size--;
					return removeNode.value;
				}
				//Situation 2: at the head of the list
				if(removeNode.prev == null && removeNode.next != null)
				{
					removeNode.next.prev = null;
					removeList.removeFirst();
					size--;
					return removeNode.value;
				}
				//Situation 3: at the end of the list
				if(removeNode.prev != null && removeNode.next == null)
				{
					removeNode.prev.next = removeNode.next;
					removeList.removeLast();
					size--;
					return removeNode.value;
				}
				//Situation 4: the only one in the list
				removeList.clear();
				size--;
				return removeNode.value;
			}
			removeNode = removeNode.next;
		}
		//if the node has not been found, return null
		return null;
	}
	/**
	 * This method clears all the pairs stored in the HashTableMap
	 */
	@Override
	public void clear() {
		for(int i = 0; i < this.TABLE_CAPACITY; i++)
		{
			//set every collision linked list to null
			this.hashPairs[i] = null;
		}
		//update the size
		size = 0;
	}
}
