package com.phs1437.debugger;

public class Test
{
	public static void main(String[] args)
	{
		isEven(3);
		isEven(2);

		count_to_5();
		print2();
	}

	/* Trivial example: function that prints 2 */

	public static void print2()
	{
		BuiltInTester tester = new BuiltInTester(true); // Initializes + enables
														// the tester
		int numberToPrint = 3;
		/* Setting up the basic test */
		tester.expecting(numberToPrint, 3, 2, "numberToPrint",
				"public void print2(String)", Integer.class, Integer.class);
		/*
		 * BUG BELOW: numberToPrint isn’t 2, so the function has the wrong
		 * output.
		 */
		System.out.println(numberToPrint);
		tester.log("numberToPrint", numberToPrint);
	}

	public static void count_to_5()
	{
		BuiltInTester tester = new BuiltInTester(true); // Initializes + enables
														// the tester
		int numberList[] = { 1, 2, 3, 4, 4 };
		/* Setting up the basic test */
		tester.expecting(numberList, new int[] { 1, 2, 3, 4, 4 }, new int[] {
				1, 2, 3, 4, 5 }, "numberList", "count_to_5()", Integer.class,
				Integer.class);
		/*
		 * BUG BELOW: numberList doesn’t count from 1 to 5, so the function
		 * prints “1,2,3,4,4” instead of “1,2,3,4,5”
		 */
		for (int i : numberList)
		{
			System.out.print(i + " ");
		}
		System.out.println();
		tester.log("numberList", numberList);
	}

	public static boolean isEven(int x)
	{
		BuiltInTester tester = new BuiltInTester(true);

		tester.startProfile("A1");
		tester.expecting(x, -1, false, "x", "public boolean isEven(int)",
				Integer.class, Boolean.class);
		tester.expecting(x, 0, true, "x", "public boolean isEven(int)",
				Integer.class, Boolean.class);
		tester.expecting(x, 1, false, "x", "public boolean isEven(int)",
				Integer.class, Boolean.class);
		tester.expecting(x, 2, true, "x", "public boolean isEven(int)",
				Integer.class, Boolean.class);
		tester.expecting(x, 3, false, "x", "public boolean isEven(int)",
				Integer.class, Boolean.class);
		System.out.println(tester.stopProfile("A1") / 1000000.0f + " ms");

		if (x % 2 == 0)
		{
			tester.log("x", true);
			return true;
		}

		tester.log("x", false);
		return false;
	}
}
