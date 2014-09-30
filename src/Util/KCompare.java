package Util;

import java.util.Comparator;

public class KCompare implements Comparator<KNNUserVector>{

	
	@Override
	public int compare(KNNUserVector o1, KNNUserVector o2) {
		if(o1.similarity < o2.similarity)
			return -1;
		else if(o1.similarity == o2.similarity)
			return 0;
		else return 1;
		
	}

}
