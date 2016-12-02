package com.bms.pm.apriwithpart.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bms.pm.apriwithpart.partition.AprioriPartition;
import com.bms.pm.apriwithpart.partition.AprioriPartitonUtils;
import com.bms.pm.apriwithpart.partition.CreatePartition;

import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

public class AprioriPartitionTest {

	final static Logger logger = Logger.getLogger(AprioriPartitionTest.class);

	public static void main(String[] args) {

		AprioriPartitonUtils apUtils = new AprioriPartitonUtils();

		Scanner sc = new Scanner(System.in);

		logger.debug("Partitioning Started...");

		logger.debug("Enter file name");
		String fileName = "AITData"; // sc.next();
		logger.debug(fileName);

		String path = ".\\src\\test\\resources\\discretizeddata\\";

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

		CreatePartition c1 = new CreatePartition();
		
		ArrayList<File> listOfFileObj = c1.createPartition(path, fileName, noOfPartition, partitionDetails);

		logger.debug("Enter min support 0.0 to 0.99 range ");

		double minSup = 0.4;// sc.nextDouble();

		logger.debug("min support :" + minSup);

		AprioriPartition ApPartAlgo = new AprioriPartition();

		List<Itemsets> results = ApPartAlgo.callAprioriAlgorith(listOfFileObj, minSup, noOfPartition, path, fileName);

		String finaloutput = path + listOfFileObj.get(0).getName() + "out_finalouput";
		
		List<int[]> dbdata = apUtils.readDataFromFile(path + fileName);

		ApPartAlgo.generateSecondFP(results, finaloutput,dbdata, minSup, noOfPartition);

	}

}
