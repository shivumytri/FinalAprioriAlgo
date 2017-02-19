package com.bms.pm.apriwithpart.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94;
import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;

/**
 * Example of how to mine all association rules with FPGROWTH and save the
 * result to a file, from the source code.
 * 
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestAllAssociationRules_FPGrowth_saveToFile {

	public static void main(String[] arg) throws IOException {

		Scanner sc = new Scanner(System.in);

		System.out.println("FP Growth Apriori Algorithm Started...");

		System.out.println("Enter file name");
		String filename = "alldata";// sc.next();

		String input = ".\\src\\test\\resources\\discretizeddata\\" + filename;
		String output = ".\\src\\test\\resources\\finaloutput\\" + filename + "_out";

		System.out.println("Enter min support 0.0 to 0.99 range ");

		double minsupp = 0.4; // sc.nextDouble();

		System.out.println("min support :" + minsupp);

		AlgoFPGrowth fpgrowth = new AlgoFPGrowth();
		fpgrowth.runAlgorithm(input, output, minsupp);
		fpgrowth.printStats();

		System.out.println("Successfully completed..");
	}

	public static String fileToPath(String filename) throws UnsupportedEncodingException {
		URL url = MainTestAllAssociationRules_FPGrowth_saveToFile.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
	}
}
