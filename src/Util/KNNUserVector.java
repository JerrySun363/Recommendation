package Util;

public class KNNUserVector {
public ObjectVector uv = null;
public double similarity=0;

public KNNUserVector(ObjectVector uv, double similarity) {
	super();
	this.uv = uv;
	this.similarity = similarity;
}



public ObjectVector getVector() {
	return uv;
}
public void setVector(ObjectVector uv) {
	this.uv = uv;
}
public double getSimilarity() {
	return similarity;
}
public void setSimilarity(double similarity) {
	this.similarity = similarity;
}


}
