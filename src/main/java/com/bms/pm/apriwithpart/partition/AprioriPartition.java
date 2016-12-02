package com.bms.pm.apriwithpart.partition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class AprioriPartition {

	final static Logger logger = Logger.getLogger(AprioriPartition.class);

	AprioriPartitonUtils apUtils = new AprioriPartitonUtils();

	public List<Itemsets> callAprioriAlgorith(ArrayList<File> listoffile, double minsup, int noofpartition, String path,
			String filname) {

		List<Itemsets> results = new ArrayList<Itemsets>();

		for (int i = 0; i < listoffile.size(); i++) {

			File inputfile = listoffile.get(i);

			String input = path + inputfile.getName();

			// String output = ".\\src\\test\\resources\\finaloutput\\" +
			// inputfile.getName() + "out";

			try {
				AlgoApriori apriori = new AlgoApriori();
				// apriori.runAlgorithm(minsup, input, output);
				Itemsets t = apriori.runAlgorithm(minsup, input, null);
				apriori.printStats();
				results.add(t);

			} catch (IOException e) {
				logger.error(" Exception while reading file. " + e.getMessage());
			}
		}
		return results;
	}

	public void generateSecondFP(List<Itemsets> results, String outputfilename, List<int[]> dbdata, double minsupp,
			int noofpartition) {
		
		List<Itemset> candidatesK = new ArrayList<>();

		logger.debug(results.get(0).getLevels());
		logger.debug(results.get(1).getLevels());
		logger.debug(results.get(2).getLevels());

		List<String> allcombinations = apUtils.getallCombinations(noofpartition);

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
							nextLevelComb.addAll(apUtils.calltocreateCombination(one, itemset1.getLevels()));
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
							nextLevelComb.addAll(apUtils.calltocreateCombination(one, temp));
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
						nextLevelComb.addAll(apUtils.calltocreateCombination(one, temp));
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
		logger.debug("Successfully completed");
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
					apUtils.saveItemsetToFile(candidate, writer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
