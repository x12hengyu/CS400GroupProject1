default: run

run: prepareRun clean

prepareRun: compile; java Frontend

compile: prepareMovieDataReader prepareBackend prepareFrontend

test: testData testBackend testFrontend clean

testFrontend: compile TestFrontend.class
	java TestFrontend

testBackend: prepareMovieDataReader prepareBackend TestBackend.class
	java TestBackend

testData: prepareMovieDataReader TestMovieAndMovieDataReader.class
	java TestMovieAndMovieDataReader

# test
TestFrontend.class: TestFrontend.java
	javac TestFrontend.java

TestBackend.class: TestBackend.java
	javac TestBackend.java

TestMovieAndMovieDataReader.class: TestMovieAndMovieDataReader.java
	javac TestMovieAndMovieDataReader.java;

# DataSupport
prepareMovieDataReader: MovieInterface.class MovieDataReaderInterface.class Movie.class  MovieDataReader.class

MovieDataReader.class: MovieDataReader.java
	javac MovieDataReader.java

MovieDataReaderInterface.class: MovieDataReaderInterface.java
	javac MovieDataReaderInterface.java

Movie.class: Movie.java
	javac Movie.java

MovieInterface.class: MovieInterface.java
	javac MovieInterface.java
	
# BackendSupport
prepareBackend: MapADT.class BackendInterface.class BackendHashTable.class Backend.class

Backend.class: Backend.java
	javac Backend.java

MapADT.class: MapADT.java
	javac MapADT.java

BackendHashTable.class: BackendHashTable.java
	javac BackendHashTable.java

BackendInterface.class: BackendInterface.java
	javac BackendInterface.java

# FrontendSupport
prepareFrontend: Frontend.class

Frontend.class: Frontend.java
	javac Frontend.java

clean:
	$(RM) *.class
