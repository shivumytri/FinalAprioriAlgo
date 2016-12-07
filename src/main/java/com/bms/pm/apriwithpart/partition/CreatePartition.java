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

	public ArrayList<File>  createPartition(String path, String fileName, int noOfPartition, int[] partitionDetails) {
	
		ArrayList<File> listOfFileObj = null;

		try {	

			File f = new File(path + fileName);
			FileReader fr = new FileReader(f);
			BufferedReader bf = new BufferedReader(fr);

			listOfFileObj = new ArrayList<File>();
			for (int i = 0; i < noOfPartition; i++) {
				File output = new File(path +"partition_" + fileName + i);
				if (output.exists()) {
					if (output.delete()) {
						logger.debug(output.getName() + " is deleted!");
					} else {
						logger.debug("Delete operation is failed.");
					}
				}

				listOfFileObj.add(output);
			}

			BufferedWriter bw = null;

			String line = bf.readLine();

			while (line != null) {

				ArrayList<String> data = getPartitionDataForEachFile(line, partitionDetails);			

				for (int i = 0; i < partitionDetails.length; i++) {
					bw = new BufferedWriter(new FileWriter(listOfFileObj.get(i), true));
					bw.write(data.get(i));
					bw.write("\n");
					bw.close();
				}

				line = bf.readLine();
			}

			bf.close();
			logger.debug("Partition Created Successfully.");			

		} catch (FileNotFoundException e) {
			logger.error("File not found. Please scan in new file.");
		} catch (Exception e) {
			logger.error("" + e.getMessage());
		}
		
		return listOfFileObj;
	}
	
	private ArrayList<String> getPartitionDataForEachFile(String line, int[] partitionDetails) throws Exception {

		ArrayList<String> result = new ArrayList<String>();

		StringTokenizer dataLine = new StringTokenizer(line);
		int noofcolumns = dataLine.countTokens();
		int totalparitionsize = 0;

		for (int i = 0; i < partitionDetails.length; i++) {
			totalparitionsize += partitionDetails[i];
		}

		if (totalparitionsize == noofcolumns) {

			for (int j = 0; j < partitionDetails.length; j++) {

				String test = "";
				boolean last = false;

				for (int i = 0; i < partitionDetails[j]; i++) {

					if (i == partitionDetails[j] - 1) {
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

	

}
