package com.bms.pm.apriwithpart.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Scanner;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;

/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestApriori_saveToFile {

	public static void main(String [] arg) throws IOException{
		
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Apriori Started...");		

		System.out.println("Enter file name");
		String filename =  "AITData";//sc.next();
		
		String input = ".\\src\\test\\resources\\discretizeddata\\" + filename;
		String output = ".\\src\\test\\resources\\finaloutput\\"+ filename +"_out";  // the path for saving the frequent itemsets found
		
		System.out.println("Enter min support 0.0 to 0.99 range ");

		double minsup = 0.4; //sc.nextDouble();

		System.out.println("min support :" + minsup);
		
		// Applying the Apriori algorithm
		AlgoApriori apriori = new AlgoApriori();		
		apriori.runAlgorithm(minsup, input, output);		
		apriori.printStats();
		
		System.out.println("Successfully completed..");
		
		sc.close();

	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestApriori_saveToFile.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
