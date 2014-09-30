package predictor;

import java.util.PriorityQueue;

import Util.KNNUserVector;

public class CustomerPredictor implements Predictor{
	/**
	 * The intuition of this method is that 
	 * customers should like to rate items around its own average score.
	 * For example, if its average score is 4, then the items' should
	 * be around 4. Thus, we think weight of the user himself should give more 
	 * consideration.
	 */
	double average = 0;
	
	public CustomerPredictor(){
		this(3);
	}
	
	public CustomerPredictor(double average){
		this.average=average;
	}
	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	@Override
	public double predictUser(int movieID, PriorityQueue<KNNUserVector> knn) {
		double score = 0.0;
		double weight = 0.0;
        double score_backup =0.0;
       
		
		for (KNNUserVector user : knn) {
			double raw = 0;
			if (user.getVector().getRecords().containsKey(movieID)) {
				raw = user.getVector().getRecords().get(movieID)-user.getVector().getAvgScore();
			} else {
				raw = 0;
			}
			score += raw * user.similarity;
			weight += user.similarity;
			score_backup += raw;
		}
		if (weight > 0.0)
			return this.average + score / weight ;
		else
			return this.average + score_backup/knn.size();
	}
	

}
