package similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import Util.ObjectVector;

public class DotSimilarity implements CalSimilarity {
	private boolean useAvg = false;

	public boolean isUseAvg() {
		return useAvg;
	}

	public void setUseAvg(boolean useAvg) {
		this.useAvg = useAvg;
	}

	@Override
	public double calSimilarity(ObjectVector query, ObjectVector uv) {
		HashMap<Integer, Byte> queryVector = query.getRecords();
		HashMap<Integer, Byte> userVector = uv.getRecords();

		Set<Integer> all = new HashSet<Integer>();
		all.addAll(queryVector.keySet());
		all.retainAll(userVector.keySet());

		double simiTotal = 0.0;
		double norm1 = (useAvg ? query.getAvgScore() : 3);
		double norm2 = (useAvg ? uv.getAvgScore() : 3);

		for (int key : all) {
			double d1 = 0;
			double d2 = 0;
			d1 = queryVector.get(key) - norm1;
			d2 = userVector.get(key) - norm2;
			simiTotal += d1 * d2;
		}
		return simiTotal;
	}

}
