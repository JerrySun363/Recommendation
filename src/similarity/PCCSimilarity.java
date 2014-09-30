package similarity;

import Util.ObjectVector;
/**
 * Calculate the PCC similarity. Similar to Cosine similarity.
 * Simply assume the empty cells to be the average score of the user.
 * 
 * @author Jerry
 *
 */
public class PCCSimilarity implements CalSimilarity {
    CosineSimilarity cs = new CosineSimilarity();
	@Override
	public double calSimilarity(ObjectVector query, ObjectVector uv) {
		double similarity = 0;
		cs.setUseAvg(true);
		cs.calSimilarity(query, uv);
		return similarity;
	}

}
