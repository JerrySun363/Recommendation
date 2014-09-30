package predictor;

import java.util.PriorityQueue;

import Util.KNNUserVector;

public interface Predictor {

	abstract double predictUser(int movieID, PriorityQueue<KNNUserVector> knn);
}
