import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.StringReader;
import java.io.Reader;
import java.io.FileReader;

/**
 * This class create a Frontend object. It will utilized Backend Object
 * 
 * @author Nitit Jongsawatsataporn (Frontend Developer)
 */
public class Frontend {

    // Backend object
    BackendInterface backend;
    int mode;
    // List of genre and rating
    List<String> genre; // List of name of all genres
    List<Boolean> gSelect; // List of boolean whether genre is selected or not
    List<Boolean> rSelect; // List of boolean whether rating is selected or not
    // Other stuff
    Scanner in; // Scanner to be used from user
    boolean changePage = true; // Detect whether we loop to the same page or different one
    int position = 1; // Keep position of main page

    // This part is for debugging.
    public static void main(String[] args) {
      Object frontend =  new Frontend();
      try {
        FileReader inputFileReader = new FileReader("movies.csv");
        ((Frontend)frontend).run(new Backend(inputFileReader));
      }
      catch(Exception e) {
        System.out.println("File not found!");
      }
    }

    /**
     * The construtor. This will do nothing but initialized all value with null. It
     * will set those value again when you run the program.
     */
    public Frontend() {

    }

    /**
     * This method is taken from outside source. This would clear the terminal
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * This method run the program front end. It will do the following 1) Set all
     * intial variable including setting back end (with no genre and all rating) 2)
     * Save all type of Genre (taken from backend) 3) Then it will run the program
     * 
     * @param backend the backend we want to use
     */
    public void run(BackendInterface backend) {
        mode = 0; // Set in base mode
        this.backend = backend;
        genre = backend.getAllGenres(); // Get all possible Genres
        gSelect = new ArrayList<Boolean>();
        for (int i = 0; i < genre.size(); i++)
            gSelect.add(false);
        rSelect = new ArrayList<Boolean>();
        for (int i = 0; i < 11; i++) {
            rSelect.add(true);
            backend.addAvgRating(((Integer) i).toString());
        }
        in = new Scanner(System.in);
        run();
    }

    /**
     * This a helper method to run the program. It will look up which page it should
     * navigated to then calling the method associated to that page.
     */
    private void run() {
        clearScreen(); // Reset the screen to blank
        if (mode == 0) // Main page
            mainPage();
        else if (mode == 1) // Genre Page
            genrePage();
        else if (mode == 2) // Rating Page
            ratingPage();
    }

    /**
     * This method maintain the mainpage of this program
     * First, It will check either this comefrom other page or this page. If it come from mainpage
     * then it will keep the same position of displayed movies. Otherwise, it would reset position
     * back to 1
     * Second, it will show the movie within the rating and genres. If none satisfy, it will show
     * a message designating that no movie satisfied the list.
     * Third, it will receive command. If command include x, it will exit the program. If command include
     * g, it will navigate to genre page. If command include r, it will navigate to rating page. If
     * input is the number, it will change position of display movie to that number. (or error message
     * if the position has no movie. If none of those, it will show error message and receive input again.
     * Finally, it would go to run() again to navigate to other/this page.
     */
    private void mainPage() {
        //If come from other page, it will reset the position
        if (changePage)
            position = 1;
        changePage = false;
        boolean correctInput = false; //A boolean checking whether input is correct or not
        //Welcome Message
        System.out.println("-------------Welcome to MovieMapper by CG Red group!---------------");
        System.out.println("-----------------------This is main page---------------------------");
        System.out.println("The current selection of movies with genre, rating, and ranking is here.");
        System.out.println("The total number of movies with selected genre/rating is " + backend.getNumberOfMovies());
        List<Movie> curList = backend.getThreeMovies(position - 1); //Asking backend for list of movie
        System.out.println();
        if (!(curList == null || backend.getNumberOfMovies() == 0 || curList.size() == 0)) {
            for (int i = 0; i < curList.size(); i++) {
                MovieInterface movie = curList.get(i); //Get ith movie
                System.out.println("(" + (position + i) + ") " + movie.getTitle() + " (" + movie.getYear() + ") "
                        + movie.getDirector() + ": " + movie.getDescription() + " (" + movie.getAvgVote() + ")");
              }
        } else {
            //Error message in case it return null list or list with 0 members.
            System.out.println("There is no movie that have genres/ratings/position you selected");
        }
        System.out.println();
        //Display the possible command
        System.out.println("Select the ranking by inputing the number");
        System.out.println("You can naviagate to Genre selection by press g.");
        System.out.println("Or navigate to rating selection by press r.");
        System.out.println("Or press x to terminate the program.");
        System.out.println("Wrong input might leads to unexpected behevior");

        //Loop until we get the correct input
        while (!correctInput) {
            correctInput = true; //Set to true. Will change to false if format is wrong
            System.out.println("Please input the command. Number for genre. x back to main page");
            String input = in.nextLine();
            if (input.contains("x")) {
                //If contains x, then it would display bye bye message and return out
                System.out.println("Thank you for using program!");
                return;
            } else if (input.contains("g")) {
                //Navigate to the genre page
                mode = 1;
                changePage = true; //To show that this come from different page
            } else if (input.contains("r")) {
                //Navigate to the rating page
                mode = 2;
                changePage = true; //To show that this come from different page
            } else {
                try {
                    int rank = Integer.parseInt(input); //Convert it to integer
                    position = rank;
                } catch (Exception e) {
                    //This means the output is not integer. It's wrong formatted!
                    System.out.println("The format is wrong!");
                    correctInput = false;
                }
            }
        }
        run(); //Use run() to navigate to another/same page
    }

    /**
     * This method maintain the genre page. In this page, user can select a number besides genre.
     * Then, the backend will be updated according to that list. User then can press x to traverse
     * back to main page and see the result of update.
     * To do that, program will first introduce that this is genre selection page along with 
     * instruction. Then, if user input contains x, it will leave back to main page. Otherwise, it
     * would convert it to number, and select/deselect genre according to that number. In case number
     * out of bound or wrong formatted, program will display error message, and prompt user to enter
     * command again.
     */
    private void genrePage() {
        boolean correctInput = false;
        //Welcome message for page
        System.out.println("Welcome to genre selection page!");
        System.out.println("The following is the genre you can select/deselect");
        System.out.println("Please select the genre by typing number according to genre.");
        //Display genre either selected or not
        for (int i = 0; i < genre.size(); i++) {
            System.out.print("(" + (i + 1) + "): " + genre.get(i) + " (");
            if (!gSelect.get(i))
                System.out.print("not selected");
            else
                System.out.print("already selected");
            System.out.println(")");
        }
        System.out.println("----------------------------------------------");
        System.out.println("Press x to navigate back to main page.");

        //Loop until getting correct input
        while (!correctInput) {
            correctInput = true;
            System.out.println("Please input the command. Number for genre. x back to main page");
            String input = in.nextLine();
            if (!input.contains("x")) {
                try {
                    int pos = Integer.parseInt(input); //Convert it to integer
                    pos--;
                    if (pos >= genre.size() || pos < 0) {
                        //This means the integer inputted is out of bound
                        correctInput = false;
                        System.out.println("Data Out of Range");
                    }
                    else {
                        if (gSelect.get(pos) == false) {
                            gSelect.set(pos, true); //Set genre to true
                            backend.addGenre(genre.get(pos)); //Add that genre for backend
                        } else {
                            gSelect.set(pos, false); //Set this genre to false
                            backend.removeGenre(genre.get(pos)); //Remove that genre from backend
                        }
                    }
                } catch (Exception e) {
                    //This means the input is not integer. Wrong formatted!
                    correctInput = false;
                    System.out.println("The format is wrong");
                }
            } else
                mode = 0; //Goes back to main page if it contains x
        }
        run(); //Use run() to navigate to another/same page
    }

    /**
     * This method maintain the rating page. In this page, user can select a number besides rating.
     * Then, the backend will be updated according to that list. User then can press x to traverse
     * back to main page and see the result of update.
     * To do that, program will first introduce that this is rating selection page along with 
     * instruction. Then, if user input contains x, it will leave back to main page. Otherwise, it
     * would convert it to number, and select/deselect rating according to that number. In case number
     * out of bound or wrong formatted, program will display error message, and prompt user to enter
     * command again.
     */
    private void ratingPage() {
        boolean correctInput = false;
        //Welcome message for page
        System.out.println("Welcome to rating selection page!");
        System.out.println("The following is the rating you can select/deselect");
        System.out.println("Please select the rating by typing number.");
        System.out.println("Note that not selecting any rating = selecting all rating");
        //Display rating either selected or not
        for (int i = 0; i < 11; i++) {
            if(i != 10)
                System.out.print("(" + (i) +".00-" + i + ".99): (");
            else
                System.out.print("(" + (i) +".00): (");
            if (!rSelect.get(i))
                System.out.print("not selected");
            else
                System.out.print("already selected");
            System.out.println(")");
        }
        System.out.println("----------------------------------------------");
        System.out.println("Press x to navigate back to main page.");

        //Loop until getting correct input
        while (!correctInput) {
            correctInput = true;
            System.out.println("Please input the command. Number for genre. x back to main page");
            String input = in.nextLine();
            if (!input.contains("x")) {
                try {
                    int pos = Integer.parseInt(input); //Convert it to integer
                    if (pos > 10 || pos < 0) {
                        //This means the integer inputted is out of bound
                        correctInput = false;
                        System.out.println("Data Out of Range");
                    }
                    else {
                        if (rSelect.get(pos) == false) {
                            rSelect.set(pos, true); //Set rating to true
                            backend.addAvgRating(Integer.toString(pos)); //Update rating change to backend
                        } else {
                            rSelect.set(pos, false); //Set rating back to false
                            backend.removeAvgRating(Integer.toString(pos)); //Remove that rating from backend
                        }
                    }
                } catch (Exception e) {
                    //This means the input is not integer. Wrong formatted!
                    correctInput = false;
                    System.out.println("The format is wrong");
                }
            } else
                mode = 0; //Goes back to main page if it contains x
        }
        run(); //Use run() to navigate to another/same page
    }

}
