package com.bms.pm.apriwithpart.partition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ecyrd.speed4j.StopWatch;

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

			itemsets.printItemsets();

			apriori.printStats();

			lstOfItmSetForEachPartition.add(itemsets);

		}
		return lstOfItmSetForEachPartition;
	}

	public Itemsets checkSupportCount(List<Itemsets> results, List<int[]> dbdata, int minsupRelative) {

		List<Itemset> candidatesK = new ArrayList<>();

		// get single item sets of the partition
		ArrayList<Integer> frequentSingleItemset = apUtils.getSingleItemSetOfPartition(results, 1);

		totalCandidateCount += frequentSingleItemset.size();

		candidatesK = apUtils.generateCandidate2(results);

		Itemsets patterns = new Itemsets("FREQUENT ITEMSETS");

		// add all itemsets of partition output
		patterns = addAllItemsetOfPartition(results);

		List<Itemset> level = null;

		int k = 2;

		do {

			if (k != 2) {
				int previousLevel = k;
				level = patterns.getLevels().get(--previousLevel);
				Collections.sort(level,Collections.reverseOrder());
				candidatesK = apUtils.generateCandidateSizeK(level);
			}

			// we add the number of candidates generated to the total
			totalCandidateCount += candidatesK.size();

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
							// we will try to find the next item of candidate
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
							// continue scanning the transaction if the current
							// item
							// is larger than the one that we search in
							// candidate.
						} else if (item > candidate.itemset[pos]) {
							// logger.info("skippng .......................");
							continue loopCand;
						}

					}
				}
			}
			// We build the level k+1 with all the candidates that have
			// a support higher than the minsup threshold.
			level = new ArrayList<Itemset>();
			for (Itemset candidate : candidatesK) {
				// if the support is > minsup
				if (candidate.getAbsoluteSupport() >= minsupRelative) {
					itemsetCount++;
					// add the candidate
					level.add(candidate);
					// the itemset is frequent so save it into results
					// saveItemset(candidate);
					patterns.addItemset(candidate, candidate.size());
				}
			}
			// we will generate larger itemsets next.
			k++;
		} while (level.isEmpty() == false);

		return patterns;
	}

	private List<Itemset> combinePartitionDataWithCurrent(List<Itemset> level, Itemsets patterns, int previousLevel) {

		List<Itemset> partitionItemsets = patterns.getLevels().get(previousLevel);
		level.addAll(partitionItemsets);
		Collections.sort(level,  Collections.reverseOrder());

		//for (Itemset oldItmSet : partitionItemsets) {
		//	int pos = getPositionToPlace(oldItmSet.getItems(), level);
		//	level.add((pos - 1), oldItmSet);
		//}
		return level;
	}

	private int getPositionToPlace(int[] oldItmSet, List<Itemset> level) {

		for (int j = 0; j < level.size(); j++) {

			int[] inTestArryCur = level.get(j).getItems();
			
			for (int i = 0; i < inTestArryCur.length; i++) {
				if (inTestArryCur[i] == oldItmSet[i] ) {
					continue;
				} else if (inTestArryCur[i] != oldItmSet[i]) {
					continue;
				} else {
					break;
				}
			}
		}
		return 1;
	}

	private Itemsets addAllItemsetOfPartition(List<Itemsets> results) {

		Itemsets allItemsets = new Itemsets("Final out put");

		for (Itemsets test : results) {
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