package com.bms.pm.apriwithpart.partition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bms.pm.apriwithpart.datadiscretizaton.NCR;

import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.tools.MemoryLogger;

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
	
	void saveItemsetToFile(Itemset itemset, BufferedWriter writer) throws IOException {

		// if the result should be saved to a file
		if (writer != null) {
			writer.write(itemset.toString() + " #SUP: " + itemset.getAbsoluteSupport());
			writer.newLine();
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
	public void printStats(int totalCandidateCount, long endTimestamp, long startTimestamp, int itemsetCount ) {
		logger.debug("=============  APRIORI - STATS =============");
		logger.debug(" Candidates count : " + totalCandidateCount);
		//logger.debug(" The algorithm stopped at size " + (k - 1)
		//		+ ", because there is no candidate");
		logger.debug(" Frequent itemsets count : " + itemsetCount);
	//	logger.debug(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
		logger.debug(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
		logger.debug("===================================================");
	}

}
