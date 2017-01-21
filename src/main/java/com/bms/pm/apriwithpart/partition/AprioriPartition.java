package com.bms.pm.apriwithpart.partition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class AprioriPartition {

	final static Logger logger = Logger.getLogger(AprioriPartition.class);

	AprioriPartitonUtils apUtils = new AprioriPartitonUtils();

	private int totalCandidateCount = 0; // number of candidate generated
											// during last execution
	protected long startTimestamp; // start time of last execution
	protected long endTimestamp; // end time of last execution
	private int itemsetCount; // itemset found during last execution

	public List<Itemsets> callAprioriAlgorith(ArrayList<File> listOfFileObj, double minSup, int noOfPartition,
			String filePath, boolean storeIntermediatoryResultToFile) throws IOException {

		List<Itemsets> lstOfItmSetForEachPartition = new ArrayList<Itemsets>();

		for (int i = 0; i < listOfFileObj.size(); i++) {

			File partitionName = listOfFileObj.get(i);

			String partitonInpRef = filePath + partitionName.getName();

			String partitonOutRef = filePath + partitionName.getName() + "out";

			AlgoApriori apriori = new AlgoApriori();

			if (storeIntermediatoryResultToFile) {
				apriori.runAlgorithm(minSup, partitonInpRef, partitonOutRef);
			}

			Itemsets itemsets = null;
			itemsets = apriori.runAlgorithm(minSup, partitonInpRef, null);

			//itemsets.printItemsets();

			apriori.printStats();

			lstOfItmSetForEachPartition.add(itemsets);

		}
		return lstOfItmSetForEachPartition;
	}

	public Itemsets checkSupportCount(List<Itemsets> results, List<int[]> dbdata, int minsupRelative) {

		List<Itemset> candidatesK = new ArrayList<>();

		candidatesK = apUtils.generateCandidate2(results);

		Itemsets patterns = new Itemsets("FREQUENT ITEMSETS");

		// add all itemsets of partition output
		patterns = addAllItemsetOfPartition(results);

		List<Itemset> level = null;
		List<Itemset> perGenItmset = null;

		int k = 2;
		int start = 0;

		do {

			if (k != 2) {
				int previousLevel = k;
				level = patterns.getLevels().get(--previousLevel);
				Collections.sort(level, Collections.reverseOrder());

				try {
					perGenItmset = patterns.getLevels().get(k);
				} catch (IndexOutOfBoundsException e) {
					perGenItmset = new ArrayList<Itemset>();
				}
				candidatesK = apUtils.generateCandidateSizeK(level, perGenItmset);
			}

			// we add the number of candidates generated to the total
			totalCandidateCount += candidatesK.size();

			if (k == 2 || perGenItmset.size() != 0 ) {
				level = getFrequentItemsets(candidatesK, dbdata, minsupRelative);
				start = k;
			} else
				level = candidatesK;

			patterns.addItemsetList(level, k);

			// we will generate larger itemsets next.
			k++;
		} while (level.isEmpty() == false);

		patterns = getFrequentItemsets(patterns, dbdata, minsupRelative, ++start);

		return patterns;
	}

	private Itemsets getFrequentItemsets(Itemsets patterns, List<int[]> dbdata, int minsupRelative,int start) {

		Itemsets pattern = new Itemsets("Final output");
		
		pattern = patterns;
		
		ArrayList<ArrayList<Itemset>> levels = patterns.getLevels();

		int k = start;
		for (int l=start; l < levels.size(); l++) {

			for (int[] transaction : dbdata) {
				// for each candidate:
				loopCand: for (Itemset candidate : levels.get(l)) {
					// a variable that will be use to check if
					// all items of candidate are in this transaction
					int pos = 0;
					// for each item in this transaction
					for (int item : transaction) {
						// if the item correspond to the current item of
						// candidate
						if (item == candidate.itemset[pos]) {
							// we will try to find the next item of
							// candidate
							// next
							pos++;
							// if we found all items of candidate in this
							// transaction
							if (pos == candidate.itemset.length) {
								// we increase the support of this candidate
								candidate.support++;
								continue loopCand;
							}
							// Because of lexical order, we don't need to
							// continue scanning the transaction if the
							// current
							// item
							// is larger than the one that we search in
							// candidate.
						} else if (item > candidate.itemset[pos]) {
							// logger.info("skippng
							// .......................");
							continue loopCand;
						}

					}
				}
			}

			List<Itemset> level = new ArrayList<Itemset>();
			for (Itemset candidate : levels.get(l)) {
				// if the support is > minsup
				if (candidate.getAbsoluteSupport() >= minsupRelative) {
					itemsetCount++;
					// the itemset is frequent so save it into results
					level.add(candidate);
				}
			}

			pattern.addItemsetListNew(level, k);
			k++;
		}

		return pattern;

	}

	private List<Itemset> getFrequentItemsets(List<Itemset> candidatesK, List<int[]> dbdata, int minsupRelative) {

		for (int[] transaction : dbdata) {
			// for each candidate:
			loopCand: for (Itemset candidate : candidatesK) {
				// a variable that will be use to check if
				// all items of candidate are in this transaction
				int pos = 0;
				// for each item in this transaction
				for (int item : transaction) {
					// if the item correspond to the current item of
					// candidate
					if (item == candidate.itemset[pos]) {
						// we will try to find the next item of
						// candidate
						// next
						pos++;
						// if we found all items of candidate in this
						// transaction
						if (pos == candidate.itemset.length) {
							// we increase the support of this candidate
							candidate.support++;
							continue loopCand;
						}
						// Because of lexical order, we don't need to
						// continue scanning the transaction if the
						// current
						// item
						// is larger than the one that we search in
						// candidate.
					} else if (item > candidate.itemset[pos]) {
						// logger.info("skippng
						// .......................");
						continue loopCand;
					}

				}
			}
		}

		List<Itemset> level = new ArrayList<Itemset>();
		for (Itemset candidate : candidatesK) {
			// if the support is > minsup
			if (candidate.getAbsoluteSupport() >= minsupRelative) {
				itemsetCount++;
				// the itemset is frequent so save it into results
				level.add(candidate);
			}
		}

		return level;

	}

	private Itemsets addAllItemsetOfPartition(List<Itemsets> results) {

		Itemsets allItemsets = new Itemsets("Final out put");

		for (Itemsets test : results) {

			itemsetCount += test.getItemsetsCount();
			totalCandidateCount += test.getTotalCandidateCount();

			for (int i = 0; i < test.getLevels().size(); i++) {
				for (Itemset test3 : test.getLevels().get(i)) {
					allItemsets.addItemset(test3, i);
				}
			}
		}
		return allItemsets;
	}

	public int getTotalCandidateCount() {
		return totalCandidateCount;
	}

	public void setTotalCandidateCount(int totalCandidateCount) {
		this.totalCandidateCount = totalCandidateCount;
	}

	public int getItemsetCount() {
		return itemsetCount;
	}

	public void setItemsetCount(int itemsetCount) {
		this.itemsetCount = itemsetCount;
	}

}