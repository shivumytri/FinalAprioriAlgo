package com.bms.pm.apriwithpart.partition;

import java.util.Comparator;

import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;

public class IndexZeroComparator implements Comparator<Itemset> {

	@Override
	public int compare(Itemset o1, Itemset o2) {
		
		Integer t1=o1.itemset[0];
		Integer t2 = o2.itemset[0];
		
		return t1.compareTo(t2) ;
	}

}
