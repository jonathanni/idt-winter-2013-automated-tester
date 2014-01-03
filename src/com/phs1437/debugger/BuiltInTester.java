package com.phs1437.debugger;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * TODO: Implement tracking of symbols in code
 * TODO: Implement custom streams (such as outputting to file)
 * TODO: Implement code profiling (optional)
 */

public class BuiltInTester implements Debugger
{

	/**
	 * Indicates whether or not the builtin tester is enabled.
	 */

	private boolean isEnabled;

	private HashMap<String, ArrayList<Object>> expectedValues = new HashMap<String, ArrayList<Object>>();
	private HashMap<String, Object> givenValues = new HashMap<String, Object>();

	/**
	 * Default constructor. Options enabled: false
	 */

	public BuiltInTester()
	{
		this(false);
	}

	/**
	 * Constructor with one argument. Options enabled: choice
	 */

	public BuiltInTester(boolean enable)
	{
		if (enable)
			enable();
		else
			disable();
	}

	/**
	 * Test function: allows the variable to change, being tested only when the
	 * log function is called.
	 */

	public void expecting(Object mutableInputValue,
			Object mutablePossibleValue, String expectedString,
			String variableID)
	{
	    givenValues.put(variableID, mutableInputValue);
		ArrayList<Object> tempList = new ArrayList<Object>();
		tempList.add(mutablePossibleValue);
		
		exptectedValues.put(expectedString, tempList);
	}

	public void expecting(Object[] mutableInputValue,
			Object[] mutablePossibleValue, String expectedString,
			String variableID)
	{
	}

	/*
	 * Example usage: if (inputValue > 2) { BuiltInTester.log("return true");
	 * return true; }
	 */

	/**
	 * Log what is expected (given in the expecting* functions) and if the block
	 * of code PASSED or FAILED.
	 */
	public void log(String expectedString)
	{
		System.out.println(expectedString);
		// TODO: Implement piping to any OutputStream
	}

	/**
	 * Enable the builtin tester. Any functions called with the builtin tester
	 * will register after it is enabled.
	 */
	public void enable()
	{
		setEnabled(true);
	}

	/**
	 * Disable the builtin tester. Any functions called with the builtin tester
	 * will be ignored after it is disabled.
	 */
	public void disable()
	{
		setEnabled(false);
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	private void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
}
