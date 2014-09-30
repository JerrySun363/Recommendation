package Util;

import java.util.HashMap;

public interface ObjectVector {
	public double getAvgScore();

	public void setAvgScore(double avgScore);

	public int getSize();

	public void setSize(int size);
	
	public void addRecord(int id, byte score);
	
	public HashMap<Integer, Byte> getRecords();
	
	public void setRecords(HashMap<Integer, Byte> records);
	
}
