package com.bms.pm.apriwithpart.partition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

			// itemsets.printItemsets();

			apriori.printStats();

			lstOfItmSetForEachPartition.add(itemsets);

		}
		return lstOfItmSetForEachPartition;
	}

	public Itemsets checkSupportCount(List<Itemsets> results, List<int[]> dbdata, int minsupRelative) {

		List<Itemset> newCandidatesItemSets = new ArrayList<>();

		newCandidatesItemSets = apUtils.generateCandidate2(results);

		Itemsets patterns = new Itemsets("FREQUENT ITEMSETS");

		// add all itemsets of partition output
		patterns = addAllItemsetOfPartition(results);

		List<Itemset> previousLevelFQItemsets = null;
		List<Itemset> currentLevelFQItemSets = null;

		int k = 2;
		int start = 0;

		do {

			if (k != 2) {
				int previousLevel = k;
				previousLevelFQItemsets = patterns.getLevels().get(--previousLevel);
				Collections.sort(previousLevelFQItemsets, Collections.reverseOrder());

				try {					
					currentLevelFQItemSets = patterns.getLevels(k);
				} catch (NullPointerException e) {
					currentLevelFQItemSets = new ArrayList<Itemset>();
				}

				// this method generates next level itemset from the previous
				// level itemset.
				// it takes two parameter previousLevelFQItemsets and
				// currentLevelFQItemSets(valid only if that level has FQ while
				// partitioning.)
				// currentLevelFQItemSets is used to generate only new fQ
				// Itemsets.
				newCandidatesItemSets = apUtils.generateCandidateSizeK(previousLevelFQItemsets, currentLevelFQItemSets);
			}

			// we add the number of candidates generated to the total
			totalCandidateCount += newCandidatesItemSets.size();

			Itemsets tempoaryItemsets = new Itemsets("FREQUENT ITEMSETS");

			if (k == 2 || k == 3 || start == 3) {
				
				tempoaryItemsets = getFrequentItemsets(tempoaryItemsets, dbdata, minsupRelative);
				
				patterns.addtempoaryItemsetsToOrignalitemsets(tempoaryItemsets);
				
				currentLevelFQItemSets = getFrequentItemsets(newCandidatesItemSets, dbdata, minsupRelative);
				
				start = 0;
				
				tempoaryItemsets = new Itemsets("FREQUENT ITEMSETS");
				
			} else {				
				tempoaryItemsets.addItemsetList(newCandidatesItemSets, k);
				start += 1;

			}

			patterns.addItemsetList(currentLevelFQItemSets, k);

			// we will generate larger itemsets next.
			k++;
		} while (currentLevelFQItemSets.isEmpty() == false);

		// patterns = getFrequentItemsets(patterns, dbdata, minsupRelative, 4);

		return patterns;
	}

	private Itemsets getFrequentItemsets(Itemsets tempoaryItemsets, List<int[]> dbdata, int minsupRelative) {		

		Map<Integer, ArrayList<Itemset>> levels = tempoaryItemsets.getLevels();

		for (Integer key : levels.keySet()) {
			for (int[] transaction : dbdata) {
				// for each candidate:
				List<Itemset> candidatesK = levels.get(key);
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
			for (Itemset candidate : levels.get(key)) {
				// if the support is > minsup
				if (candidate.getAbsoluteSupport() >= minsupRelative) {
					itemsetCount++;
					// the itemset is frequent so save it into results
					level.add(candidate);
				}
			}
			tempoaryItemsets.addItemsetListNew(level, key);

		}
		return tempoaryItemsets;

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