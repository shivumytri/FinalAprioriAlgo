package com.shivumytri.datadiscretizaton;

public class NCR {

	// Take the factorial of an integer
	private int factorial(int num) {
		int fact = 1;
		for (int i = 1; i <= num; i++)
			fact = fact * i;
		return fact;
	}

	// Get the nCr
	private int nCr(int n, int r) {
		return factorial(n) / (factorial(n - r) * factorial(r));
	}

	// Get the combinations of the array
	public int[][] nCrArray(int[] array, int r) {
		int n = array.length;
		// Choose r from the length of the array
		int ncr = nCr(n, r);
		int[][] result = new int[ncr][r];
		int result_index = 0;
		// BASE CASE
		if (r == 1) {
			// Each value in the array is a result
			for (; result_index < array.length; result_index++)
				result[result_index][0] = array[result_index];
		}
		// All other cases
		else {
			// Iterate through each of the starting values
			for (int i = 0; i < array.length - r + 1; i++) {
				// Create the sub-array
				int[] recursivearray = new int[n - i - 1];
				// Copy into the sub-array all values after the active value
				System.arraycopy(array, i + 1, recursivearray, 0, n - i - 1);
				// Calculate the sub-combinations (recurse)
				int[][] subarrays = nCrArray(recursivearray, r - 1);
				// Create the results for this active value
				for (int j = 0; j < subarrays.length; j++) {
					// Augment the active value and sub-combinations
					result[result_index][0] = array[i];
					System.arraycopy(subarrays[j], 0, result[result_index], 1, subarrays[j].length);
					result_index++;
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		
		NCR test= new NCR();
		
		int[] array = { 0, 1, 2, 3 };
		int r = 3;
		int[][] answer = test.nCrArray(array, r);
		for (int i = 0; i < answer.length; i++) {
			for (int j = 0; j < answer[i].length - 1; j++) {
				System.out.print(answer[i][j] + ",");
			}
			System.out.println(answer[i][answer[i].length - 1]);
		}
	}

}