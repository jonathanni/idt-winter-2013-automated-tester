package com.phs1437.debugger;

public class Test
{
	public static void main(String[] args)
	{
		isEven(3);
		isEven(2);
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
