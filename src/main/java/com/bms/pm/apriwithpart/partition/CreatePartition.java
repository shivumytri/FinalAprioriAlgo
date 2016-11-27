package com.bms.pm.apriwithpart.partition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

import com.bms.pm.apriwithpart.datadiscretizaton.NCR;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class CreatePartition {

	final static Logger logger = Logger.getLogger(CreatePartition.class);

	public void createPartition() {

		Scanner sc = new Scanner(System.in);
		ArrayList<File> listoffile = null;

		try {

			System.out.println("Partitioning Started...");

			System.out.println("Enter file name");
			String filename = "BMSCEdata"; // sc.next();

			System.out.println("Enter number of partition");
			int noofpartition = 3;// sc.nextInt();
			int[] partitiondetails = new int[noofpartition];

			// for (int i = 0; i < noofpartition; i++) {
			// System.out.println("Enter number of columns, per partition_" +
			// filename + (i + 1) );
			partitiondetails[0] = 8;// sc.nextInt();
			partitiondetails[1] = 8;// sc.nextInt();
			partitiondetails[2] = 9;// sc.nextInt();
			// }

			String fullname = ".\\discretizeddata\\" + filename;

			File f = new File(fullname);
			FileReader fr = new FileReader(f);
			BufferedReader bf = new BufferedReader(fr);

			listoffile = new ArrayList<File>();
			for (int i = 0; i < noofpartition; i++) {
				File output = new File(".\\finaloutput\\partition_" + filename + i);
				if (output.exists()) {
					if (output.delete()) {
						System.out.println(output.getName() + " is deleted!");
					} else {
						System.out.println("Delete operation is failed.");
					}
				}

				listoffile.add(output);
			}

			BufferedWriter bw = null;

			String line = bf.readLine();

			while (line != null) {

				ArrayList<String> data = getPartitionDataForEachFile(line, partitiondetails);

				// logger.debug(data.toString());

				for (int i = 0; i < partitiondetails.length; i++) {
					bw = new BufferedWriter(new FileWriter(listoffile.get(i), true));
					bw.write(data.get(i));
					bw.write("\n");
					bw.close();
				}

				line = bf.readLine();
			}

			bf.close();
			System.out.println("Partition Created Successfully.");
			callAprioriAlgorith(listoffile, sc, noofpartition, fullname);

		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file.");
		} catch (Exception e) {
			System.err.println("" + e.getMessage());
		}
	}

	public List<Itemsets> callAprioriAlgorith(ArrayList<File> listoffile, Scanner sc, int noofpartition,
			String fullname) {

		System.out.println("Enter min support 0.0 to 0.99 range ");

		double minsup = 0.4;// sc.nextDouble();

		logger.debug("min support :" + minsup);

		List<Itemsets> results = new ArrayList<Itemsets>();

	
		String finaloutput = null;

		finaloutput = ".\\finaloutput\\" + listoffile.get(0).getName() + "out_finalouput";

		for (int i = 0; i < listoffile.size(); i++) {

			File inputfile = listoffile.get(i);

			String input = ".\\finaloutput\\" + inputfile.getName();

			//String output = ".\\finaloutput\\" + inputfile.getName() + "out";

			try {
				AlgoApriori apriori = new AlgoApriori();
				// apriori.runAlgorithm(minsup, input, output);
				Itemsets t = apriori.runAlgorithm(minsup, input, null);// apriori.runAlgorithm(0.1,
																		// output,
																		// null);
				apriori.printStats();
				results.add(t);

			} catch (IOException e) {
				System.err.println(" Exception while reading file. ");
			}

		}

		generateSecondFP(results, finaloutput, fullname, minsup, noofpartition);

		return results;
	}

	public void generateSecondFP(List<Itemsets> results, String outputfilename, String inputfilename, double minsupp,
			int noofpartition) {

		List<int[]> dbdata = readDataFromFile(inputfilename);
		List<Itemset> candidatesK = new ArrayList<>();

		System.out.println(results.get(0).getLevels());
		System.out.println(results.get(1).getLevels());
		System.out.println(results.get(2).getLevels());

		List<String> allcombinations = getallCombinations(noofpartition);

		Map<String, List<Itemset>> listOfNextLevelComb = new HashMap<>();

		for (int i = 0; i < results.size(); i++) {

			Itemsets x = results.get(i);

			List<Itemset> nextLevelComb = new ArrayList<>();

			for (List<Itemset> test : x.getLevels()) {
				for (Itemset o : test) {
					o.setAbsoluteSupport(0);
				}
				nextLevelComb.addAll(test);
			}

			listOfNextLevelComb.put("" + i, nextLevelComb);
		}

		for (String str : allcombinations) {

			// String str = "01";

			if (str.length() % 2 == 0) {

				if (str.length() / 2 == 1) {

					Itemsets itemset0 = results.get(Integer.parseInt("" + str.charAt(0)));
					Itemsets itemset1 = results.get(Integer.parseInt("" + str.charAt(1)));

					itemset0.printItemsets(0);
					itemset1.printItemsets(0);

					List<Itemset> nextLevelComb = new ArrayList<>();

					for (List<Itemset> test : itemset0.getLevels()) {
						for (Itemset one : test) {
							nextLevelComb.addAll(calltocreateCombination(one, itemset1.getLevels()));
						}
					}
					listOfNextLevelComb.put(str, nextLevelComb);
				} else {

					List<Itemset> firstComb = listOfNextLevelComb.get(str.substring(0, str.length() / 2));
					List<Itemset> secComb = listOfNextLevelComb.get(str.substring((str.length() / 2), str.length()));

					if (firstComb != null && secComb != null) {
						List<ArrayList<Itemset>> temp = new ArrayList<ArrayList<Itemset>>();
						temp.add((ArrayList<Itemset>) secComb);

						List<Itemset> nextLevelComb = new ArrayList<>();

						for (Itemset one : firstComb) {
							nextLevelComb.addAll(calltocreateCombination(one, temp));
						}

						listOfNextLevelComb.put(str, nextLevelComb);
					}
				}

			} else {

				double d = str.length() / 2.0;
				int mid = (int) Math.ceil(d);

				List<Itemset> firstComb = listOfNextLevelComb.get(str.substring(0, mid));
				List<Itemset> secComb = listOfNextLevelComb.get(str.substring(mid, str.length()));

				if (firstComb != null && secComb != null) {
					List<ArrayList<Itemset>> temp = new ArrayList<ArrayList<Itemset>>();
					temp.add((ArrayList<Itemset>) secComb);

					List<Itemset> nextLevelComb = new ArrayList<>();

					for (Itemset one : firstComb) {
						nextLevelComb.addAll(calltocreateCombination(one, temp));
					}

					listOfNextLevelComb.put(str, nextLevelComb);
				}
			}
		}

		for (String str : listOfNextLevelComb.keySet()) {
			if (listOfNextLevelComb.get(str) != null) {
				candidatesK.addAll(listOfNextLevelComb.get(str));
			}

		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputfilename));
			Collections.sort(candidatesK, new IndexZeroComparator());
			Collections.sort(candidatesK, new SizeComparator());
			getSupportCountofNewItems(candidatesK, dbdata, (int) Math.ceil(minsupp * dbdata.size()), writer);
			writer.close();
		} catch (IOException e) {
			logger.debug(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Successfully completed");
	}

	private List<String> getallCombinations(int noofpartition) {

		List<String> allcombinations = new ArrayList<>();

		NCR getallCombination = new NCR();
		int[] array = new int[noofpartition];
		for (int i = 0; i < noofpartition; i++) {
			array[i] = i;
		}
		for (int i = 2; i <= noofpartition; i++) {
			int[][] t1 = getallCombination.nCrArray(array, i);
			for (int j = 0; j < t1.length; j++) {
				String str = "";
				for (int k = 0; k < t1[j].length - 1; k++) {
					str += t1[j][k];
				}
				str += t1[j][t1[j].length - 1];
				allcombinations.add(str);
			}
		}
		return allcombinations;
	}

	private void getSupportCountofNewItems(List<Itemset> candidatesK, List<int[]> dbdata, int minsupRelative,
			BufferedWriter writer) {

		for (int[] transaction : dbdata) {

			// for each candidate:
			loopCand: for (Itemset candidate : candidatesK) {

				// logger.debug(candidate);
				// a variable that will be use to check if
				// all items of candidate are in this transaction
				int pos = 0;
				// for each item in this transaction
				for (int item : transaction) {
					// if the item correspond to the current item of candidate
					if (item == candidate.itemset[pos]) {
						// we will try to find the next item of candidate next
						pos++;
						// if we found all items of candidate in this
						// transaction
						if (pos == candidate.itemset.length) {
							// we increase the support of this candidate
							candidate.support++;
							continue loopCand;
						}
						// Because of lexical order, we don't need to
						// continue scanning the transaction if the current item
						// is larger than the one that we search in candidate.
					} else if (item > candidate.itemset[pos]) {
						// logger.debug("Skipping ......"+item +"::"+
						// candidate.itemset[pos]);
						continue loopCand;
					}

				}
			}
		}

		for (Itemset candidate : candidatesK) {
			// if the support is > minsup
			if (candidate.getAbsoluteSupport() >= minsupRelative) {
				// logger.debug(candidate.toString());
				try {
					saveItemsetToFile(candidate, writer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	void saveItemsetToFile(Itemset itemset, BufferedWriter writer) throws IOException {

		// if the result should be saved to a file
		if (writer != null) {
			writer.write(itemset.toString() + " #SUP: " + itemset.getAbsoluteSupport());
			writer.newLine();
		}
	}

	private List<Itemset> calltocreateCombination(Itemset one, List<ArrayList<Itemset>> temp) {

		List<Itemset> test3 = new ArrayList<Itemset>();

		try {
			for (List<Itemset> test2 : temp) {
				for (Itemset one2 : test2) {
					one2.setAbsoluteSupport(0);
					test3.add(one2.add(one));
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		return test3;

	}

	private ArrayList<String> getPartitionDataForEachFile(String line, int[] partitiondetails) throws Exception {

		ArrayList<String> result = new ArrayList<String>();

		StringTokenizer dataLine = new StringTokenizer(line);
		int noofcolumns = dataLine.countTokens();
		int totalparitionsize = 0;

		for (int i = 0; i < partitiondetails.length; i++) {
			totalparitionsize += partitiondetails[i];
		}

		if (totalparitionsize == noofcolumns) {

			for (int j = 0; j < partitiondetails.length; j++) {

				String test = "";
				boolean last = false;

				for (int i = 0; i < partitiondetails[j]; i++) {

					if (i == partitiondetails[j] - 1) {
						last = true;
					}

					if (last) {
						test = test + dataLine.nextToken();
					} else {
						test = test + dataLine.nextToken() + " ";
					}

				}
				result.add(test);
			}
		} else {
			throw new Exception(
					"Partition Failed : Total partition columns does not added upto total of columns in file.");
		}
		return result;

	}

	private List<int[]> readDataFromFile(String inputfile) {

		List<int[]> database = null;
		int databaseSize = 0;

		database = new ArrayList<int[]>();

		BufferedReader reader = null;
		FileReader fileReader = null;

		try {
			fileReader = new FileReader(inputfile);
			reader = new BufferedReader(fileReader);
			String line;
			while (((line = reader.readLine()) != null)) {
				if (line.isEmpty() == true || line.charAt(0) == '#' || line.charAt(0) == '%' || line.charAt(0) == '@') {
					continue;
				}
				String[] lineSplited = line.split(" ");
				int transaction[] = new int[lineSplited.length];
				for (int i = 0; i < lineSplited.length; i++) {
					Integer item = Integer.parseInt(lineSplited[i]);
					transaction[i] = item;
				}
				database.add(transaction);
				databaseSize++;
			}
			reader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			logger.debug(e);
		} catch (IOException e) {
			logger.debug(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				logger.debug(e);
			}
		}
		logger.debug("database size :" + databaseSize);
		return database;
	}

}
