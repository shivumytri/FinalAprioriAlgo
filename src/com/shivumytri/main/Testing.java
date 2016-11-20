package com.shivumytri.main;
import java.util.ArrayList;
import java.util.List;

import com.shivumytri.datadiscretizaton.NCR;

public class Testing {

	public static void main(String[] args) {
		
		Testing t1=new Testing();
		
		List<String> test=t1.getallCombinations(4);
		
		//System.out.println(test);
		String str="012";
		double d =  str.length()/2.0;
		int mid=   (int) Math.ceil(d);
		
		System.out.println(test);
		
		System.out.println(d);
		System.out.println(mid);

	}

	
	private List<String> getallCombinations(int noofpartition) {
		
		List<String> allcombinations = new ArrayList<>();
		
		NCR getallCombination = new NCR();
		int[] array=new int[noofpartition];
		for (int i = 0; i < noofpartition; i++) {			
			array[i] = i;		
		}		
		for (int i = 2; i <= noofpartition; i++) {			
			int[][] t1=getallCombination.nCrArray(array, i);
			for (int j = 0; j < t1.length; j++) {
				String str="";
				for (int k = 0; k < t1[j].length - 1; k++) {
					str +=t1[j][k];					
				}
				str += t1[j][t1[j].length-1];
				allcombinations.add(str);
			}
		}
		return allcombinations;
	}
}
