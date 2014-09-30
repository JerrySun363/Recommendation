package predictor;

import java.util.PriorityQueue;

import Util.KNNUserVector;

public class MeanPredictor implements Predictor {

	@Override
	public double predictUser(int movieID, PriorityQueue<KNNUserVector> knn) {
		double score = 0.0;
		double weight = 0.0;

		for (KNNUserVector user : knn) {
			double raw = 0;
			if (user.getVector().getRecords().containsKey(movieID)) {
				raw = user.getVector().getRecords().get(movieID);
			} else {
				raw = user.getVector().getAvgScore();
			}
			score += raw * 1 * 1.0;
			weight += 1;
		}
		return score / weight;
	}

}
