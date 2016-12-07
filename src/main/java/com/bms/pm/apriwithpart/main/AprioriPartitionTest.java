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

		AprioriPartitonUtils apUtils = new AprioriPartitonUtils();
		StopWatch sw = new StopWatch();
		Scanner sc = new Scanner(System.in);

		// Path of folder where discretized data is present
		String filePath = ".\\src\\test\\resources\\discretizeddata\\";

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
		partitionDetails[0] = 3;// sc.nextInt();
		partitionDetails[1] = 3;// sc.nextInt();
		partitionDetails[2] = 3;// sc.nextInt();
		// }

		CreatePartition createPartition = new CreatePartition();
		ArrayList<File> listOfFileObj = createPartition.createPartition(filePath, fileName, noOfPartition,
				partitionDetails);

		logger.debug("Enter min support 0.0 to 0.99 range ");
		double minSup = 0.4;// sc.nextDouble();
		logger.debug("min support :" + minSup);

		// gives itemsets of each partition
		sw.start();
		AprioriPartition ApPartAlgo = new AprioriPartition();

		List<Itemsets> lstOfItmSetForEachPartition = ApPartAlgo.callAprioriAlgorith(listOfFileObj, minSup,
				noOfPartition, filePath, false);
		sw.stop();
		// logger.debug("Time took to generate frequent itemsets of all
		// partition "+ sw.getElapsedTime() + " ns");
		logger.debug("Time took to generate frequent itemsets of all partition " + sw.toString() );

		// gives candidates set of after combination of each partition
		sw.start();
		List<Itemset> candidatesK = ApPartAlgo.generatingCandidateItemSets(lstOfItmSetForEachPartition, noOfPartition);
		sw.stop();
		logger.debug("Time took to generate candidates itemsets of the partition " + sw.toString());
		// logger.debug("Time took to generate candidates itemsets of the
		// partition "+ sw.getElapsedTime()/1000 +" ms");

		// file name to store final out.
		String finaloutput = filePath + listOfFileObj.get(0).getName() + "out_finalouput";

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(finaloutput));

			Collections.sort(candidatesK, new IndexZeroComparator());
			Collections.sort(candidatesK, new SizeComparator());

			List<int[]> dbdata = apUtils.readDataFromFile(filePath + fileName);
			sw.start();
			ApPartAlgo.getSupportCountofNewItems(candidatesK, dbdata, (int) Math.ceil(minSup * dbdata.size()), writer);
			sw.stop();
			logger.debug("Time took to find all frequent itemsets " + sw.toString());
			// logger.debug("Time took to find all frequent itemsets "+
			// sw.getElapsedTime()/1000 + " ms");
			logger.debug("Successfully frequrent item sets are generated.");

			writer.close();
		} catch (IOException e) {
			logger.error(e);
		}

	}

}
