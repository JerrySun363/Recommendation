package similarity;

import Util.ObjectVector;


public interface CalSimilarity {
   
	public abstract double calSimilarity(ObjectVector query, ObjectVector uv);
}
