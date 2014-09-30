import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import predictor.CustomerPredictor;
import predictor.MeanPredictor;
import predictor.Predictor;
import predictor.WeightedPredictor;
import similarity.CalSimilarity;
import similarity.CosineSimilarity;
import similarity.DotSimilarity;
import similarity.PCCSimilarity;
import Util.KCompare;
import Util.KNNUserVector;
import Util.MovieVector;
import Util.ObjectVector;
import Util.UserVector;

public class Recommender {
	public HashMap<Integer, ObjectVector> movies = new HashMap<Integer, ObjectVector>();
	public HashMap<Integer, ObjectVector> users = new HashMap<Integer, ObjectVector>();

	public HashMap<Integer, PriorityQueue<KNNUserVector>> dotSimilarity = new HashMap<Integer, PriorityQueue<KNNUserVector>>();
	public HashMap<Integer, PriorityQueue<KNNUserVector>> cosineSimilarity = new HashMap<Integer, PriorityQueue<KNNUserVector>>();
	public PrintWriter out;
	public ArrayList<String> queries = new ArrayList<String>();

	/**
	 * The choices of SIMILARITY measures. More to be added here.
	 */
	public static int DOT = 0;
	public static int COS = 1;
	public static int PCC = 2;
	/**
	 * Control the calculation of prediction on.
	 */
	public static int CUS  = 4;
	public static int MEAN = 5;
	public static int WEIGHT=6;
	
	private static int USER = 3;
	private static int MOVIE =7;

	public static void main(String[] argv) {
		String trainFile = argv[0];
		String testFile = argv[1];
		String outputFile = argv[2];

		Recommender rc = new Recommender();
		rc.constrcutTrain(trainFile);
		rc.constructQuerySet(testFile);
		
		// 10- Mean
		long start = System.currentTimeMillis();
		rc.processQuery("DOT-10" + outputFile, 10, Recommender.MEAN,
				Recommender.PCC,Recommender.MOVIE);
		long end1 = System.currentTimeMillis();
		System.out.println(end1 - start);/**/
		//long end1 = System.currentTimeMillis();
		
		// 100-mean
				rc.processQuery("DOT-100-" + outputFile, 100, Recommender.MEAN,
				Recommender.PCC,Recommender.MOVIE);
		long end2 = System.currentTimeMillis();
		System.out.println(end2 - end1);

		// 500-mean
		rc.processQuery("DOT-500-" + outputFile, 500, Recommender.MEAN,
				Recommender.PCC,Recommender.MOVIE);
		long end3 = System.currentTimeMillis();
		System.out.println(end3 - end2);
		
		

		// 10-cosine
		rc.processQuery("COS-10-mean-" + outputFile, 10, Recommender.WEIGHT,
				Recommender.PCC,Recommender.MOVIE);
		long end4 = System.currentTimeMillis();
		System.out.println(end4 - end3);
		// 100-cosine
		rc.processQuery("COS-100-mean-" + outputFile, 100, Recommender.WEIGHT,
				Recommender.PCC,Recommender.MOVIE);
		long end5 = System.currentTimeMillis();
		System.out.println(end5 - end4);

		// 500-cosine
		rc.processQuery("COS-500-mean-" + outputFile, 500, Recommender.WEIGHT,
				Recommender.PCC,Recommender.MOVIE);
		long end6 = System.currentTimeMillis();
		System.out.println(end6 - end5);
		/* 
		// 10-cosine
		rc.processQuery("COS-10-weight-" + outputFile, 10, Recommender.WEIGHT,
				Recommender.COS,Recommender.MOVIE);
		long end7 = System.currentTimeMillis();
		System.out.println(end7 - end6);

		// 100-cosine
		rc.processQuery("COS-100-weight-" + outputFile, 100, Recommender.WEIGHT,
				Recommender.COS,Recommender.MOVIE);
		long end8 = System.currentTimeMillis();
		System.out.println(end8 - end7);

		// 500-cosine
		rc.processQuery("COS-500-weight-" + outputFile, 500, Recommender.WEIGHT,
				Recommender.COS,Recommender.MOVIE);
		long end9 = System.currentTimeMillis();
		System.out.println(end9 - end8);*/

	}

	/**
	 * This method reads in the trainFile and constructs the Movie and User
	 * vectors.
	 * 
	 * @param trainFile
	 */
	public void constrcutTrain(String trainFile) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(trainFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		scanner.nextLine();// jump the head.
		int movieID = -1;
		int userID = -1;
		byte score = -1;
		MovieVector mv = null;
		int totalScore = 0;

		while (scanner.hasNextLine()) {
			String textline = scanner.nextLine();
			String[] rows = textline.split(",");
			int newMovieID = Integer.parseInt(rows[0]);
			userID = Integer.parseInt(rows[1]);
			score = Byte.parseByte(rows[2]);

			if (newMovieID != movieID) {
				movieID = newMovieID;
				if (mv != null) {
					mv.setAvgScore(totalScore*1.0 / mv.getRecords().size());
					this.movies.put(mv.getMovieID(), mv);
					this.constrcutUserVector(mv);
				}
				mv = new MovieVector();
				mv.setMovieID(newMovieID);
				totalScore = 0;
			}
			totalScore += score;
			mv.addMovie(userID, score);

		}
		// construct for the last one.
		mv.setAvgScore(totalScore*1.0 / mv.getRecords().size());
		this.movies.put(mv.getMovieID(), mv);
		this.constrcutUserVector(mv);

		scanner.close();

		// now calculate the average score of user lists
		for (ObjectVector uv : users.values()) {
			uv.setAvgScore(((UserVector) uv).totalScore / uv.getSize());
		}

	}

	/**
	 * This method reads in the constructed Movie Vector and constructs the User
	 * Vector.
	 * 
	 * @param mv
	 */
	private void constrcutUserVector(MovieVector mv) {
		Iterator<Integer> ite = mv.getRecords().keySet().iterator();
		while (ite.hasNext()) {
			int userId = ite.next();
			byte score = mv.getRecords().get(userId);
			if (users.containsKey(userId)) {
				UserVector uv = (UserVector) users.get(userId);
				uv.addRecord(mv.getMovieID(), score);
				uv.totalScore += score;
				uv.setSize(uv.getSize() + 1);
			} else {
				UserVector uv = new UserVector();
				uv.setUserID(userId);
				uv.addRecord(mv.getMovieID(), score);
				uv.totalScore += score;
				uv.setSize(1);
				users.put(userId, uv);
			}
		}
	}

	private void constructQuerySet(String testFile) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(testFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		scanner.nextLine();

		while (scanner.hasNext()) {
			queries.add(scanner.nextLine());
		}
		scanner.close();

	}

	public void processQuery(String outputFile, int k, int predict,
			int similarity, int direction) {
		dotSimilarity.clear();

		try {
			out = new PrintWriter(
					new BufferedWriter(new FileWriter(outputFile)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<String> ite = queries.iterator();
		Predictor predictor;
		if (predict == Recommender.WEIGHT) {
			predictor = new WeightedPredictor();
		} else if(predict == Recommender.MEAN){
			predictor = new MeanPredictor();
		} else{
			 predictor = new CustomerPredictor(); 
		} 
		CalSimilarity cal = null;
		if(similarity == Recommender.COS){
			cal = new CosineSimilarity();
		}else if(similarity == Recommender.DOT){
			cal = new DotSimilarity();
		}else {
			cal = new PCCSimilarity();
		}
		HashMap<Integer, ObjectVector> di  = null;
		if(direction == Recommender.USER){
			di=this.users;
		}else{
			di=this.movies;
		}
		
		while (ite.hasNext()) {
			String[] query = ite.next().split(",");
			
			int movieID = Integer.parseInt(query[0]);
			int userID = Integer.parseInt(query[1]);
			PriorityQueue<KNNUserVector> knn = null;
			if (dotSimilarity.containsKey(userID)){
				knn = this.dotSimilarity.get(userID);
			} else {
				knn = knnSearch(userID,k,di,cal);
				/*Iterator<KNNUserVector> ite2 = knn.iterator();
				System.out.println("For this :");
				while(ite2.hasNext()){
					KNNUserVector nima = ite2.next();
					
					if(direction == Recommender.USER){
					UserVector myuv =(UserVector)nima.getVector();
						
				 System.out.println(nima.getSimilarity()+","+myuv.getUserID() );
					}	else{
					MovieVector mv = (MovieVector) nima.getVector();	
				System.out.println(mv.getMovieID());
					}		
				}*/
				this.dotSimilarity.put(userID, knn);
			}
			double score = predictor.predictUser(movieID, knn);
			out.println(score);
		}

		out.flush();
		out.close();
	}

	public void processQueryCus(String outputFile, int k, int predict,
			int similarity, int direction) {
		dotSimilarity.clear();
		//THIS INGORES THE PREDICTOR parameter
		try {
			out = new PrintWriter(
					new BufferedWriter(new FileWriter(outputFile)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<String> ite = queries.iterator();
		CustomerPredictor predictor = new CustomerPredictor(); 
		 
		CalSimilarity cal = null;
		if(similarity == Recommender.COS){
			cal = new CosineSimilarity();
		}else if(similarity == Recommender.DOT){
			cal = new DotSimilarity();
		}else {
			cal = new PCCSimilarity();
		}
		HashMap<Integer, ObjectVector> di  = null;
		if(direction == Recommender.USER){
			di=this.users;
		}else{
			di=this.movies;
		}
		
		while (ite.hasNext()) {
			String[] query = ite.next().split(",");
			int movieID = Integer.parseInt(query[0]);
			int userID = Integer.parseInt(query[1]);
			PriorityQueue<KNNUserVector> knn = null;
			if (dotSimilarity.containsKey(userID)) {
				knn = this.dotSimilarity.get(userID);
			} else {
				knn = knnSearch(userID,k,di,cal);
				this.dotSimilarity.put(userID, knn);
			}
			
			predictor.setAverage(this.users.get(userID).getAvgScore());
			double score = predictor.predictUser(movieID, knn);
			out.println(score);
		}

		out.flush();
		out.close();
	}
	
	private PriorityQueue<KNNUserVector> knnSearch(int id,
			int k, HashMap<Integer, ObjectVector> di, CalSimilarity cal) {
		PriorityQueue<KNNUserVector> knn = new PriorityQueue<KNNUserVector>(k,
				new KCompare());
		
		
		ObjectVector queryUser = di.get(id);
		di.remove(id);// remove itself
		
		for (ObjectVector uv : di.values()) {
			double simi = 0;
			simi = cal.calSimilarity(queryUser, uv);
			knn.add(new KNNUserVector(uv, simi));
			if (knn.size() > k) {
				knn.poll();
			}
		}

		// add itself back
		di.put(id, queryUser);
		return knn;
	}

}