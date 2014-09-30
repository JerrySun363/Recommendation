package Util;

import java.util.HashMap;

public class UserVector implements ObjectVector{
/**
 * These are meta information for a User Vector.
 * Store the score as byte to save the space.
 */
private double avgScore = 0;
private  int size=0;
private int userID=0;
public int totalScore = 0;

HashMap<Integer, Byte> records = null;

public double getAvgScore() {
	return avgScore;
}

public void setAvgScore(double avgScore) {
	this.avgScore = avgScore;
}

public int getSize() {
	return size;
}

public void setSize(int size) {
	this.size = size;
}

public int getUserID() {
	return userID;
}

public void setUserID(int userID) {
	this.userID = userID;
}

public HashMap<Integer, Byte> getRecords() {
	return records;
}

public void setRecords(HashMap<Integer, Byte> records) {
	this.records = records;
}

public UserVector(){
	this.records = new HashMap<Integer, Byte>();
}


public void addRecord(int movieID, byte score){
	this.records.put(movieID, score);
}
	
}
