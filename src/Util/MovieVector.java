package Util;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Movie Vector stores the records from different users, the movie ID and its average score.
 * It does not store the whole sum of scores since the movies are sequentially 
 * @author Jerry
 *
 */
public class MovieVector implements ObjectVector{
	
private int movieID;
private double avgScore;
private int size;
private HashMap<Integer, Byte> records = new HashMap<Integer, Byte>();
public int totalScore = 0;

public MovieVector() {
	super();
}



public MovieVector(int movieID, double avgScore, HashMap<Integer, Byte> records) {
	super();
	this.movieID = movieID;
	this.avgScore = avgScore;
	this.records = records;
}

public HashMap<Integer, Byte> getRecords() {
	return records;
}

public void setRecords(HashMap<Integer, Byte> records) {
	this.records = records;
}

public int getMovieID() {
	return movieID;
}

public void setMovieID(int movieID) {
	this.movieID = movieID;
}

public void addMovie(int userID, byte score){
	this.records.put(userID,score); 
}

@Override
public double getAvgScore() {
	return this.avgScore;
}

@Override
public void setAvgScore(double avgScore) {
	this.avgScore = avgScore;
}

@Override
public int getSize() {
	return this.size;
}

@Override
public void setSize(int size) {
	this.size = size;
}

@Override
public void addRecord(int userId, byte score) {
	this.records.put(userId,score);
}



}
