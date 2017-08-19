package com.bms.pm.apriwithpart.partition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bms.pm.apriwithpart.datadiscretizaton.NCR;

import ca.pfv.spmf.algorithms.ArraysAlgos;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class AprioriPartitonUtils {

	final static Logger logger = Logger.getLogger(AprioriPartitonUtils.class);

	public List<String> getallCombinations(int noofpartition) {

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

	public void saveItemsetToFile(Itemsets itemsets, BufferedWriter writer) throws IOException {

		// if the result should be saved to a file

		Map<Integer, ArrayList<Itemset>> levels = itemsets.getLevels();
		for (Integer key : levels.keySet()) {
			List<Itemset> listitemset = levels.get(key);
			for (Itemset itemset : listitemset) {
				if (writer != null) {
					writer.write(itemset.toString() + " #SUP: " + itemset.getAbsoluteSupport());
					writer.newLine();
				}
			}
		}
	}

	public List<Itemset> calltocreateCombination(Itemset one, List<ArrayList<Itemset>> temp) {

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

	public List<int[]> readDataFromFile(String inputfile) {

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

	public void printStats(int totalCandidateCount, long endTimestamp, long startTimestamp, int itemsetCount) {
		logger.debug("=============  APRIORI - STATS =============");
		logger.debug(" Candidates count : " + totalCandidateCount);
		// logger.debug(" The algorithm stopped at size " + (k - 1)
		// + ", because there is no candidate");
		logger.debug(" Frequent itemsets count : " + itemsetCount);
		// logger.debug(" Maximum memory usage : " +
		// MemoryLogger.getInstance().getMaxMemory() + " mb");
		logger.debug(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
		logger.debug("===================================================");
	}

	public ArrayList<Integer> getSingleItemSetOfPartition(List<Itemsets> results, int level) {

		ArrayList<Integer> frequentSingleItemset = new ArrayList<Integer>();

		for (Itemsets itemsets : results) {

			// int whichPartition = Integer.parseInt("" +
			// partitionData.charAt(i));

			ArrayList<Itemset> test = itemsets.getLevels().get(level);

			for (Itemset singleItem : test) {
				frequentSingleItemset.add(singleItem.get(0));
			}
		}

		Collections.sort(frequentSingleItemset, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		return frequentSingleItemset;
	}

	/**
	 * This method generates candidates itemsets of size 2 based on itemsets of
	 * size 1.
	 * 
	 * @param frequent1
	 *            the list of frequent itemsets of size 1.
	 * @return a List of Itemset that are the candidates of size 2.
	 */
	public List<Itemset> generateCandidate2(List<Itemsets> results) {
		List<Itemset> candidates = new ArrayList<Itemset>();

		for (int k = 0; k < results.size(); k++) {

			Collections.sort(results.get(k).getLevels().get(1), Collections.reverseOrder());

			for (int i = 0; i < results.get(k).getLevels().get(1).size(); i++) {
				Integer item1 = results.get(k).getLevels().get(1).get(i).get(0);

				for (int l = k + 1; l < results.size(); l++) {

					for (int j = 0; j < results.get(l).getLevels().get(1).size(); j++) {
						Collections.sort(results.get(l).getLevels().get(1), Collections.reverseOrder());

						Integer item2 = results.get(l).getLevels().get(1).get(j).get(0);

						// Create a new candidate by combining itemset1 and
						// itemset2
						Itemset newItemset = new Itemset(new int[] { item1, item2 });
						candidates.add(newItemset);
					}
				}
			}
		}
		return candidates;
	}

	/**
	 * Method to generate itemsets of size k from frequent itemsets of size K-1.
	 * 
	 * @param levelK_1
	 *            frequent itemsets of size k-1
	 * @param currentLevelFQItemSets
	 *            it the generated itemset equal to any itemset in the this
	 *            list, that itemset set is ignored from the list
	 * @return itemsets of size k
	 */
	public List<Itemset> generateCandidateSizeK(List<Itemset> levelK_1, List<Itemset> currentLevelFQItemSets) {
		// create a variable to store candidates
		List<Itemset> candidates = new ArrayList<Itemset>();

		// For each itemset I1 and I2 of level k-1
		loop1: for (int i = 0; i < levelK_1.size(); i++) {
			int[] itemset1 = levelK_1.get(i).itemset;
			loop2: for (int j = i + 1; j < levelK_1.size(); j++) {
				int[] itemset2 = levelK_1.get(j).itemset;

				// we compare items of itemset1 and itemset2.
				// If they have all the same k-1 items and the last item of
				// itemset1 is smaller than
				// the last item of itemset2, we will combine them to generate a
				// candidate
				for (int k = 0; k < itemset1.length; k++) {
					// if they are the last items
					if (k == itemset1.length - 1) {
						// the one from itemset1 should be smaller (lexical
						// order)
						// and different from the one of itemset2
						if (itemset1[k] >= itemset2[k]) {
							continue loop1;
						}
					}
					// if they are not the last items, and
					else if (itemset1[k] < itemset2[k]) {
						continue loop2; // we continue searching
					} else if (itemset1[k] > itemset2[k]) {
						continue loop1; // we stop searching: because of lexical
										// order
					}
				}

				// Create a new candidate by combining itemset1 and itemset2
				int newItemset[] = new int[itemset1.length + 1];
				System.arraycopy(itemset1, 0, newItemset, 0, itemset1.length);
				newItemset[itemset1.length] = itemset2[itemset2.length - 1];

				// The candidate is tested to see if its subsets of size k-1 are
				// included in
				// level k-1 (they are frequent).
				if (allSubsetsOfSizeK_1AreFrequent(newItemset, levelK_1)) {
					Itemset newitemset = new Itemset(newItemset);
					int test = -1;
					for (Itemset itemset : currentLevelFQItemSets) {
						test = itemset.compareTo(newitemset);
						if (test == 0)
							break;
					}
					if (test != 0)
						candidates.add(newitemset);
				}
			}
		}
		return candidates; // return the set of candidates
	}

	/**
	 * Method to check if all the subsets of size k-1 of a candidate of size k
	 * are freuqnet
	 * 
	 * @param candidate
	 *            a candidate itemset of size k
	 * @param levelK_1
	 *            the frequent itemsets of size k-1
	 * @return true if all the subsets are frequet
	 */
	private boolean allSubsetsOfSizeK_1AreFrequent(int[] candidate, List<Itemset> levelK_1) {
		// generate all subsets by always each item from the candidate, one by
		// one
		for (int posRemoved = 0; posRemoved < candidate.length; posRemoved++) {

			// perform a binary search to check if the subset appears in level
			// k-1.
			int first = 0;
			int last = levelK_1.size() - 1;

			// variable to remember if we found the subset
			boolean found = false;
			// the binary search
			while (first <= last) {
				int middle = (first + last) >> 1; // >>1 means to divide by 2

				int comparison = ArraysAlgos.sameAs(levelK_1.get(middle).getItems(), candidate, posRemoved);
				if (comparison < 0) {
					first = middle + 1; // the itemset compared is larger than
										// the subset according to the lexical
										// order
				} else if (comparison > 0) {
					last = middle - 1; // the itemset compared is smaller than
										// the subset is smaller according to
										// the lexical order
				} else {
					found = true; // we have found it so we stop
					break;
				}
			}

			if (found == false) { // if we did not find it, that means that
									// candidate is not a frequent itemset
									// because
				// at least one of its subsets does not appear in level k-1.
				return false;
			}
		}
		return true;
	}

}
