package com.idt.contest.highschool.winter2014.codetotest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.idt.contest.highschool.winter2014.framework.FrameworkConstants;
import com.phs1437.debugger.BuiltInTester;

/**
 * Class containing math related utility methods
 */
public class MathUtility {

	/**
	 * Method that checks if a number is even
	 * 
	 * @param numToCheck
	 *            - the number to check
	 * @return boolean - true if the number is even, false if the number is odd
	 */
	public boolean isEven(int numToCheck) {
		BuiltInTester tester = new BuiltInTester(true);

		tester.expecting(numToCheck, 0, true, "numToCheck", "isEven",
				Integer.class, Boolean.class);
		tester.expecting(numToCheck, 1, false, "numToCheck", "isEven",
				Integer.class, Boolean.class);
		tester.expecting(numToCheck, 2, true, "numToCheck", "isEven",
				Integer.class, Boolean.class);
		tester.expecting(numToCheck, 3, false, "numToCheck", "isEven",
				Integer.class, Boolean.class);
		// divide the number by 2 and no remainder exists, the number is even
		if (numToCheck % 2 == 0) {
			tester.log("numToCheck", true);
			return true;
		} else {
			tester.log("numToCheck", false);
			return false;
		}
	}

	/**
	 * Method to compute the hypotenus of a triangle given two sides
	 * 
	 * @param a
	 *            - length of first side of triangle
	 * @param b
	 *            - length of second side of triangle
	 * @return - integer value of the hypotenus of the triangle
	 */
	public double hypotenus(double a, double b) {

		double aSquared = a * a;
		double bSquared = b * b;
		return Math.sqrt(aSquared + bSquared);
	}

	/**
	 * Method that will return the prime factors of a given number
	 * 
	 * @param n
	 *            - number you wish to derive the prime factors of
	 * @return - array of ints that represent the prime factors
	 */
	public int[] primeFactor(int number) {

		BuiltInTester tester = new BuiltInTester(true);
		tester.expecting(number, 10, new int[] { 2, 5 }, "number",
				"primeFactor", Integer.class, Integer[].class);
		tester.expecting(number, 123, new int[] { 3, 41 }, "number",
				"primeFactor", Integer.class, Integer[].class);
		List<Integer> factors = new ArrayList<Integer>();
		int divider = 2;

		// determine the prime factors of the given number
		while (divider * divider <= number) {
			if (number % divider == 0) {
				factors.add(divider);
				number /= divider;
			} else {
				divider++;
			}
		}

		if (number > 1) {
			factors.add(number);
		}

		// convert Integer List to int array
		int[] returnArray = new int[factors.size()];
		for (int i = 0; i < factors.size(); i++) {
			returnArray[i] = factors.get(i);
		}

		tester.log("number", returnArray);
		// return int array of prime factors
		return returnArray;
	}

	/**
	 * Method to multiple to simple binomials using the FOIL technique similar
	 * to (2x + 6) * (7x - 10)
	 * 
	 * @param x1
	 *            , the multiplier of the first x (2 in the example above)
	 * @param ones1
	 *            , the first ones value (6 in the example above)
	 * @param x2
	 *            , the multiplier of the second x (7 in the example above)
	 * @param ones2
	 *            , the second ones value (-10 in the example above)
	 * @return - String representation of binomial product
	 */
	public String multiplySimpleBinomials(short x1, short ones1, short x2,
			short ones2) {
		BuiltInTester tester = new BuiltInTester(true);
		tester.expecting(new short[] { x1, ones1, x2, ones2 }, new short[] { 2,
				6, 7, -10 }, "14x^2 + 22x - 60", "binomials",
				"multiplySimpleBinomials", Short[].class, String.class);
		
		tester.expecting(new short[] { x1, ones1, x2, ones2 }, new short[] { 3,
				-2, 4, 4 }, "12x^2 + 4x - 8", "binomials",
				"multiplySimpleBinomials", Short[].class, String.class);

		boolean notTheFirst = false;
		String binomialResult = "";

		// follow foil procedure and multiply all combinations
		short first, outer, inner, last;
		first = (short) (x1 * x2);
		outer = (short) (x1 * ones2);
		inner = (short) (ones1 * x2);
		last = (short) (ones1 * ones2);

		// assemble binomial string
		if (first != 0) {
			if (Math.abs(first) != 1) {
				binomialResult += first;
			}
			binomialResult += "x^2 ";
			notTheFirst = true;
		}

		if (outer + inner != 0) {
			if (outer + inner > 0 && notTheFirst) {
				binomialResult += FrameworkConstants.POSITIVE_SIGN + " ";
			} else if (notTheFirst) {
				binomialResult += FrameworkConstants.NEGATIVE_SIGN + " ";
			}

			if (Math.abs(outer + inner) != 1) {
				binomialResult += Math.abs(outer + inner);
			}
			binomialResult += "x ";
			notTheFirst = true;
		}

		if (last != 0) {
			//
			//
			//
			//
			//
			//
			// BUG below... the less than sign in the if statement should be a
			// greater than sign. Because of this switch, a positive sign will
			// show up in front of the ones place where a negative sign should
			// show up, and vice versa.
			//
			//
			//
			//
			//
			//
			if (last < 0 && notTheFirst) {
				binomialResult += FrameworkConstants.POSITIVE_SIGN + " ";
			} else if (notTheFirst) {
				binomialResult += FrameworkConstants.NEGATIVE_SIGN + " ";
			}
			binomialResult += Math.abs(last);
		}

		tester.log("binomials", binomialResult);
		return binomialResult;
	}

	/**
	 * Method used to do simple currency conversions
	 * 
	 * @param amount
	 *            - float amount of money you start with
	 * @param rate
	 *            - float exchange rate from start currency to finish currency
	 * @return - float amount of currency you finish with
	 */
	public float convertCurrency(float amount, float rate) {

		// if the amount is zero, the result will be zero
		// if the rate is zero, the result will be zero
		if (amount == 0 || rate == 0) {
			return 0;
			// if the rate is one, the amount will not be transformed
		} else if (rate == 1) {
			return amount;
			// otherwise we multiply the amount by the exchange rate
		} else {
			return amount * rate;
		}
	}

}
