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
            String line; // String of a line parsing from the reader
            String[] seq; // The seqence of strings after we split variable "line"
            int[] idx = new int[6]; // (title,year,genres,director,description,avg_vote)
            String[] header = new String[]{"title","year","genre","director","description","avg_vote"};
            // read first line
            line = readLine(inputFileReader);
            if(line == null) throw new DataFormatException("No first column");
            seq = line.split(",");
            for(int i = 0; i < seq.length; ++i){
                for(int j = 0; j < i; ++j){
                    if(seq[i].equals(seq[j])){
                        throw new DataFormatException("Duplicate Columns");
                    }
                }
            }
            int total_col = seq.length;
            // find index for each category
            for(int i = 0; i < 6; ++i) idx[i] = -1;
            for(int i = 0; i < seq.length; ++i){
                for(int j = 0; j < 6; ++j){
                    if(seq[i].equals(header[j])) idx[j] = i;
                }
            }
            for(int i = 0; i < 6; ++i){
                if(idx[i] == -1){
                    throw new DataFormatException("Important column missing: "+header[i]);
                }
            }
            // read the entire line
            // and then attempt to parse them
            while((line = readLine(inputFileReader)) != null){
                seq = line.split(",");
                ArrayList<String> sep = new ArrayList<String>();
                for(int i = 0; i < seq.length; ++i){
                    String col = "";
                    if(seq[i].length() != 0){
                        if(seq[i].charAt(0)=='\"'){
                            int j;
                            for(j = i; j < seq.length; ++j){
                                col += seq[j];
                                if(seq[j].charAt(seq[j].length()-1)=='\"'){
                                    break;
                                }
                                col += ',';
                            }
                            i = j;
                        }else{
                            col = seq[i];
                        }
                    }
                    sep.add(col);
                }
                if(sep.size() != total_col){
                    throw new DataFormatException("Some rows has less or more column than the first row");
                }
                String[] pass = new String[6];
                for(int i = 0; i < 6; ++i){
                    pass[i] = sep.get(idx[i]);
                }
                container.add(new Movie(pass));
            }
            inputFileReader.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
            throw new FileNotFoundException("File not Found");
        }catch(IOException e){
            throw new IOException("IO Exception Error");
        }
        return container;
    }
    
    /**
     * A helper method that helps reading a line from input
     * of reader
     * @param reader the reader
     * @return the next single line if available, or null if otherwise
     */
    String readLine(Reader inputFileReader) throws IOException {
        int v;
        String ans = "";
        while((v = inputFileReader.read())!=-1){
            if(v == 13) continue;
            if((char)v == '\n') return ans;
            ans += (char)v;
        }
        return null;
    }
}
