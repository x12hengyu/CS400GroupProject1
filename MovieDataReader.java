// --== CS400 File Header Information ==--
// Name: Nott Laoaron
// Email: laoaron@wisc.edu
// Team: Red
// Group: CG
// TA: Xi Chen
// Lecturer: Florian Heimerl
// Notes to Grader: This is MovieDataReader class

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * MovieDataReader reads the input information from the csv file,
 * and turn them into MovieInterfaces object
 * @author Nott Laoaron (Data Wrangler)
 */
public class MovieDataReader implements MovieDataReaderInterface {
    /**
     * This method read the data from inputFileReader
     * Format them and return the list of movies
     * @param inputFileReader the file reader that contains the file we will read
     * @return the list of movies that we read from the file
     * @throws FileNotFoundException when there is an error opening the file of inputFileReader
     * @throws IOException when there is an error while reading the file
     * @throws DataFormatException when the data format of the file is invalid
     */
    @Override
	public List<Movie> readDataSet(Reader inputFileReader) throws FileNotFoundException, IOException, DataFormatException {
        List<Movie> container = new ArrayList<Movie>();
        try{
            // read file
            //inputFileReader = new FileReader("movies.csv");
            // read the entire line
            // and then attempt to parse them
            // Note: This procedure ignores the first line of the input
            int v;
            String line = "";
            boolean firstline = true;
            int row = 1;
            while((v = inputFileReader.read())!=-1){
                if((char)v == '\n'){
                    if(!firstline){
                        ArrayList<String>[] raw_data = (ArrayList<String>[]) new ArrayList[13];
                        for(int i = 0; i < 13; ++i) raw_data[i] = new ArrayList<String>();
                        int cur = 0;
                        String[] sp = line.split(",(\\s)*");
                        int len = sp.length;
                        /*
                        for(int i = 0; i < len; ++i){
                            System.out.print(sp[i]);
                            System.out.print("||");
                        }
                        System.out.println();
                        */
                        for(int i = 0; i < len; ++i){
                            if(cur > 12){
                                throw new DataFormatException("Too many columns");
                            }
                            if(!sp[i].isEmpty() && sp[i].charAt(0) == '\"'){
                                int j;
                                for(j = i; j < len && sp[j].charAt(sp[j].length()-1) != '\"'; ++j);
                                if(i == j){
                                    raw_data[cur].add(sp[i].substring(1, sp[i].length()-1));
                                }else{
                                    raw_data[cur].add(sp[i].substring(1));
                                    for(int k = i+1; k < j; ++k){
                                        raw_data[cur].add(sp[k]);
                                    }
                                    raw_data[cur].add(sp[j].substring(0, sp[j].length()-1));
                                }
                                i = j;
                                ++cur;
                            }else{
                                raw_data[cur++].add(sp[i]);
                            }
                        }
                        // add the input into the container
                        container.add(new Movie(raw_data));
                    }
                    firstline = false;
                    line = "";
                    row++;
                }else{
                    line += (char)v;
                }
            }
            inputFileReader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
            throw new FileNotFoundException("File not Found");
        }catch(IOException e){
            throw new IOException("IO Exception Error");
        }
        return (List<Movie>) (List<? extends Movie>) container;
    }
}
