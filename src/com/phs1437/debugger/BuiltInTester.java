/**
 * @author Jonathan Ni, Diwakar Ganesan, Kent Ma
 */
package com.phs1437.debugger;

import java.lang.reflect.Array;
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
	// Mapping a input id to a value
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
	 * 
	 * @param mutablePossibleValue
	 *            A possible value for the variable.
	 * 
	 * @param expectedString
	 *            Specifies the string needed to be logged in order to pass the
	 *            test. Use {@link #log(String)} to actually log the string
	 *            specified for this.
	 * 
	 * @param variableID
	 *            A unique string that identifies the variable inputted. This is
	 *            used to differentiate between different tests simultaneously
	 *            running for {@link #log(String)}
	 * 
	 * @param functionID
	 *            A unique string to identify the function the tested code
	 *            resided in.
	 * 
	 * @param type
	 *            The data type of the variable to be tested. Accepts array
	 *            types.
	 * 
	 * @return 0 on success and 1 on failure
	 * 
	 */

	public int expecting(Object inputValue, Object possibleValue,
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

		return 0;
	}

	public int expecting(Object[] inputValue, Object[] possibleValue,
			String expectedString, String variableID, String functionID,
			Class<?> type)
	{
		if (inputValue.length != possibleValue.length)
		{
			Logger.logError("Input and expecting value arrays do not match");
			return 1;
		}

		return expecting(inputValue, possibleValue, expectedString, variableID,
				functionID, type);

		/*
		 * try { givenValues.put(expectedString, inputValue);
		 * 
		 * ArrayList<Output> tempList; if ((tempList =
		 * expectedValues.get(variableID)) == null) tempList = new
		 * ArrayList<Output>();
		 * 
		 * tempList.add(new Output(possibleValue, expectedString, type));
		 * expectedValues.put(variableID, tempList);
		 * 
		 * variableResidences.put(variableID, functionID); } catch (Exception e)
		 * { e.printStackTrace(); return 1; }
		 */
	}

	/**
	 * Log what is expected (given in the expecting* functions) and if the block
	 * of code PASSED or FAILED.
	 * 
	 * @param variableID
	 *            the ID of the variable to test for set in the expecting
	 *            functions
	 * @param expectedString
	 *            the message String that is expected
	 */
	public int log(String variableID, String expectedString)
	{
		// Find the expected output associated with the value
		ArrayList<Output> expectedOutputs = expectedValues.get(variableID);
		String functionID = variableResidences.get(variableID);

		for (Output i : expectedOutputs)
		{
			if (i.getType().isArray())
			{
				Object givenValArr = givenValues.get(variableID);
				Object expectedValArr = i.getExpectedValue();

				boolean match = true;
				for (int j = 0; j < Array.getLength(givenValArr); j++)
					if (!i.getType()
							.getEnclosingClass()
							.cast(Array.get(givenValArr, j))
							.equals(i.getType().getEnclosingClass()
									.cast(Array.get(expectedValArr, j))))
					{
						match = false;
						break;
					}

				// Given and expected values have a match
				if (match)
				{
					// Give a string representation of the values
					StringBuilder inputValue = new StringBuilder();
					inputValue.append("[");
					for (int j = 0; j < Array.getLength(givenValArr); j++)
						inputValue.append(i.getType().getEnclosingClass()
								.cast(Array.get(givenValArr, j)).toString()
								+ " ");
					inputValue.append("]");

					// Check if the expected strings match
					if (i.getExpectedString().equals(expectedString))
					{
						logInternal(true, inputValue.toString(),
								expectedString, i.getExpectedString(),
								variableID, functionID);
						return 0;
					} else
					{
						logInternal(false, inputValue.toString(),
								expectedString, i.getExpectedString(),
								variableID, functionID);
						return 1;
					}
				}
			} else
			{
				Object givenVal = givenValues.get(variableID);
				Object expectedVal = i.getExpectedValue();

				boolean match = true;
				if (!i.getType().cast(givenVal)
						.equals(i.getType().cast(expectedVal)))
					match = false;

				// Given and expected values have a match
				if (match)
				{
					// Check if the expected strings match
					if (i.getExpectedString().equals(expectedString))
					{
						logInternal(true,
								i.getType().cast(givenVal).toString(),
								expectedString, i.getExpectedString(),
								variableID, functionID);
						return 0;
					} else
					{
						logInternal(false, i.getType().cast(givenVal)
								.toString(), expectedString,
								i.getExpectedString(), variableID, functionID);
						return 1;
					}
				}
			}
		}
		// TODO: Implement piping to any OutputStream
		return -1;
	}

	private void logInternal(boolean passed, String inputVal, String inputStr,
			String expectedStr, String variableID, String functionID)
	{
		Logger.logInfo(String
				.format("Variable %s %s in function %s with value %s, given %s expecting %s",
						variableID, passed ? "PASSED" : "FAILED", functionID,
						inputVal, inputStr, expectedStr));
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
	private final Object expectedValue;
	private final String expectedString;
	private final Class<?> type;

	public Output(Object ev, String es, Class<?> type)
	{
		this.expectedValue = ev;
		this.expectedString = new String(es);
		this.type = type;
	}

	public Object getExpectedValue()
	{
		return expectedValue;
	}

	public String getExpectedString()
	{
		return expectedString;
	}

	public Class<?> getType()
	{
		return type;
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
