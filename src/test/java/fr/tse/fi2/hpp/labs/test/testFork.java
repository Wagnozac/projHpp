package fr.tse.fi2.hpp.labs.test;


import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.MergeSort;

import java.util.Arrays;

public class testFork {

	@Test
	public void test() {
		
		int[] tab = MergeSort.tabVal(1000000);
		int[] tab2 = tab;
		Arrays.sort(tab2);
		
		assert(MergeSort.mergeIt(tab)==tab2);

	}

	

	
	
}
