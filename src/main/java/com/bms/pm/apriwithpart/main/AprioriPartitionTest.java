package com.bms.pm.apriwithpart.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bms.pm.apriwithpart.partition.AprioriPartition;
import com.bms.pm.apriwithpart.partition.AprioriPartitonUtils;
import com.bms.pm.apriwithpart.partition.CreatePartition;
import com.bms.pm.apriwithpart.partition.IndexZeroComparator;
import com.bms.pm.apriwithpart.partition.SizeComparator;
import com.ecyrd.speed4j.StopWatch;

import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class AprioriPartitionTest {

	final static Logger logger = Logger.getLogger(AprioriPartitionTest.class);

	public static void main(String[] args) {

		long startTimestamp; // start time of last execution
		long endTimestamp = 0; // end time of last execution
		int itemsetCount = 0;

		AprioriPartitonUtils apUtils = new AprioriPartitonUtils();
		StopWatch sw = new StopWatch();
		Scanner sc = new Scanner(System.in);

		// Path of folder where discretized data is present
		String filePath = ".\\src\\test\\resources\\finaloutput\\";
		String fileDataPath = ".\\src\\test\\resources\\discretizeddata\\";

		logger.debug("Partitioning Started...");
		logger.debug("Enter file name");
		String fileName = "AITData"; // sc.next();
		logger.debug(fileName);
		logger.debug("Enter number of partition");
		int noOfPartition = 3;// sc.nextInt();
		logger.debug(noOfPartition);

		int[] partitionDetails = new int[noOfPartition];
		// for (int i = 0; i < noofpartition; i++) {
		// logger.debug("Enter number of columns, per partition_" +
		// filename + (i + 1) );
		// partitionDetails[0] = 8;// sc.nextInt();
		// partitionDetails[1] = 8;// sc.nextInt();
		// partitionDetails[2] = 9;// sc.nextInt();
		partitionDetails[0] = 3;// sc.nextInt();
		partitionDetails[1] = 3;// sc.nextInt();
		partitionDetails[2] = 3;// sc.nextInt();
		// }

		logger.debug("Enter min support 0.0 to 0.99 range ");
		double minSup = 0.4;// sc.nextDouble();
		logger.debug("min support :" + minSup);

		CreatePartition createPartition = new CreatePartition();
		ArrayList<File> listOfFileObj = null;
		try {
			listOfFileObj = createPartition.createPartition(filePath, fileDataPath, fileName, noOfPartition,
					partitionDetails);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// gives itemsets of each partition
		startTimestamp = System.currentTimeMillis();
		AprioriPartition ApPartAlgo = new AprioriPartition();

		List<Itemsets> lstOfItmSetForEachPartition = null;
		try {
			lstOfItmSetForEachPartition = ApPartAlgo.callAprioriAlgorith(listOfFileObj, minSup, noOfPartition, filePath,
					false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		List<int[]> dbdata = apUtils.readDataFromFile(".\\src\\test\\resources\\discretizeddata\\" + fileName);

		Itemsets patterns = new Itemsets("FREQUENT ITEMSETS");

		patterns = ApPartAlgo.checkSupportCount(lstOfItmSetForEachPartition, dbdata,
				(int) Math.ceil(minSup * dbdata.size()));

		//patterns.printItemsets();

		endTimestamp = System.currentTimeMillis();
		
		apUtils.printStats(ApPartAlgo.getTotalCandidateCount(), endTimestamp, startTimestamp, ApPartAlgo.getItemsetCount());

		// file name to store final out.
		String finaloutput = filePath + listOfFileObj.get(0).getName() + "out_finalouput";

		

	}

}
