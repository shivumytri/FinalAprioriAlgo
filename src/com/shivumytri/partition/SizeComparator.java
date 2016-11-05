package com.shivumytri.partition;

import java.util.Comparator;

import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;

public class SizeComparator implements Comparator<Itemset> {

	@Override
	public int compare(Itemset o1, Itemset o2) {		
		if(o1.itemset.length > o2.itemset.length){
			return 1;
		}else if(o1.itemset.length < o2.itemset.length){
			return -1;
		}else{
			return 0;
		}
		
	}

}
