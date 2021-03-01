run: compile test

compile: MovieDataReaderInterface.class MovieInterface.class

test: testData testBackend testFrontend

testFrontend: TestFrontend.class
	java TestFrontend
	
testBackend: TestBackend.class
	java TestBackend
	
testData: TestMovieAndMovieDataReader.class
	java TestMovieAndMovieDataReader

# test

TestFrontend.class: TestFrontend.java
	javac TestFrontend.java
	
TestBackend.class: TestBackend.java
	javac TestBackend.java

# compile

MovieDataReaderInterface.class: MovieDataReaderInterface.java
	javac MovieDataReaderInterface.java

MovieInterface.class: MovieInterface.java
	javac MovieInterface.java

clean:
	$(RM) *.class
