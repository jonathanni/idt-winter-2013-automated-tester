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

	private HashMap<String, ArrayList<Output>> expectedValues = new HashMap<String, ArrayList<Output>>();
	private HashMap<String, Object> givenValues = new HashMap<String, Object>();
	private HashMap<String, String> variableResidences = new HashMap<String, String>();

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

	public void expecting(Object inputValue, Object possibleValue,
			String expectedString, String variableID, String functionID,
			Class<?> type)
	{
		// Put the given variable value assigned to a variableID
		givenValues.put(variableID, inputValue);

		// Put the expected variable value with the expected String assigned to
		// a variableID
		ArrayList<Output> tempList;
		if ((tempList = expectedValues.get(variableID)) == null)
			tempList = new ArrayList<Output>();

		tempList.add(new Output(possibleValue, expectedString, type));
		expectedValues.put(variableID, tempList);

		// Assign a variableID to a functionID
		variableResidences.put(variableID, functionID);
	}

	public void expecting(Object[] mutableInputValue,
			Object[] mutablePossibleValue, String expectedString,
			String variableID, String functionID, Class<?> type)
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

class Output
{
	private Object expectedValue;
	private String expectedString;
	private Class<?> type;

	public Output(Object ev, String es, Class<?> type)
	{
		setExpectedValue(ev);
		setExpectedString(es);
		setType(type);
	}

	public Object getExpectedValue()
	{
		return expectedValue;
	}

	public void setExpectedValue(Object expectedValue)
	{
		this.expectedValue = expectedValue;
	}

	public String getExpectedString()
	{
		return expectedString;
	}

	public void setExpectedString(String expectedString)
	{
		this.expectedString = expectedString;
	}

	public Class<?> getType()
	{
		return type;
	}

	public void setType(Class<?> type)
	{
		this.type = type;
	}

	@Override
	public boolean equals(Object other)
	{
		return other == this
				|| ((other instanceof Output)
						&& getType().cast(((Output) other).expectedValue)
								.equals(getType().cast(expectedValue)) && ((Output) other).expectedString
							.equals(expectedString));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expectedString == null) ? 0 : expectedString.hashCode());
		result = prime * result
				+ ((expectedValue == null) ? 0 : expectedValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public String toString()
	{
		return "Output [expectedValue=" + expectedValue + ", expectedString="
				+ expectedString + ", type=" + type + "]";
	}
}
