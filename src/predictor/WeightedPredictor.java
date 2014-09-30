package predictor;

import java.util.PriorityQueue;

import Util.KNNUserVector;

public class WeightedPredictor implements Predictor {

	@Override
	public double predictUser(int movieID, PriorityQueue<KNNUserVector> knn) {
		double score = 0.0;
		double weight = 0.0;
        double score_backup =0.0;
       
		
		for (KNNUserVector user : knn) {
			double raw = 0;
			if (user.getVector().getRecords().containsKey(movieID)) {
				raw = user.getVector().getRecords().get(movieID);
			} else {
				raw = user.getVector().getAvgScore();
			}
			score += raw * user.similarity;
			weight += user.similarity;
			score_backup += raw;
		}
		if (weight > 0.0)
			return score / weight;
		else
			return score_backup/knn.size();
	}

}
