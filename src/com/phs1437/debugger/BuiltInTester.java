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

	public void expecting(int inputValue, int possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(byte inputValue, byte possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(short inputValue, short possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(long inputValue, long possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(float inputValue, float possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(double inputValue, double possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(boolean inputValue, boolean possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(char inputValue, char possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(String inputValue, String possibleValue,
			String expectedString, String variableID)
	{
	}

	public void expecting(int[] inputValue, int[] possibleValue,
			String expectedString, String variableID)
	{
	}

	/**
	 * Test function: allows the variable to change, being tested only when the
	 * log function is called.
	 */

	public void expectingMutable(Object mutableInputValue,
			Object mutablePossibleValue, String expectedString,
			String variableID)
	{
	}

	public void expectingMutable(Object[] mutableInputValue,
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
