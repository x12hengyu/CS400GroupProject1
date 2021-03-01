run: compile
	java Frontend.java
	clean

compile: MovieDataReader Backend Frontend

test: testData testBackend testFrontend clean

testFrontend: Frontend TestFrontend.class
	java TestFrontend
	
testBackend: Backend TestBackend.class
	java TestBackend
	
testData: MovieDataReader TestMovieAndMovieDataReader.class
	java TestMovieAndMovieDataReader

# test
TestFrontend.class: TestFrontend.java
	javac TestFrontend.java
	
TestBackend.class: TestBackend.java
	javac TestBackend.java
	
TestMovieAndMovieDataReader.class: TestMovieAndMovieDataReader.java
	javac TestMovieAndMovieDataReader.java;

# DataSupport
MovieDataReader: MovieDataReader.class MovieDataReaderInterface.class Movie.class MovieInterface.class

MovieDataReader.class: MovieDataReader.java
	javac MovieDataReader.java

MovieDataReaderInterface.class: MovieDataReaderInterface.java
	javac MovieDataReaderInterface.java
	
Movie.class: Movie.java
	javac Movie.java

MovieInterface.class: MovieInterface.java
	javac MovieInterface.java
	
# BackendSupport
Backend: Backend.class MapADT.class BackendHashTable.class BackendInterface.class

Backend.class: Backend.java
	javac Backend.java

MapADT.class: MapADT.java
	javac MapADT.java
	
BackendHashTable.class: BackendHashTable.java
	javac BackendHashTable.java
	
BackendInterface.class: BackendInterface.java
	javac BackendInterface.java

# FrontendSupport
Frontend: Frontend.class

Frontend.class: Frontend.java
	javac Frontend.java

clean:
	$(RM) *.class
