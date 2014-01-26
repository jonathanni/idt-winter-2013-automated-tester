/**
 * @author Jonathan Ni, Diwakar Ganesan, Kent Ma
 */
package com.phs1437.debugger;

/**
 * @author Jonathan Ni, Diwakar Ganesan, Kent Ma
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * TODO: Implement tracking of symbols in code
 * TODO: Implement custom streams (such as outputting to file)
 * TODO: Implement code profiling (optional)
 */

public class BuiltInTester implements Debugger {

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

	public BuiltInTester() {
		this(false);
	}

	/**
	 * Constructor with one argument. Options enabled: choice
	 */

	public BuiltInTester(boolean enable) {
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
	 * @param expectedOutput
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
			Object expectedOutput, String variableID, String functionID,
			Class<?> inputType, Class<?> outputType) {
		// Put the given variable value assigned to a variableID
		givenValues.put(variableID, inputValue);

		// Put the expected variable value with the expected String assigned to
		// a variableID

		ArrayList<Output> tempList;
		if ((tempList = expectedValues.get(variableID)) == null)
			tempList = new ArrayList<Output>();

		// possibleValue is all the values the input could be, so inputType is
		// the data type of possibleValue
		tempList.add(new Output(possibleValue, expectedOutput, possibleValue.getClass(),
				expectedOutput.getClass()));

		expectedValues.put(variableID, tempList);

		// Assign a variableID to a functionID
		variableResidences.put(variableID, functionID);

		return 0;
	}

	// TODO Fix this implementation
	/*
	 * public int expecting(Object[] inputValue, Object[] possibleValue, String
	 * expectedOutput, String variableID, String functionID, Class<?> type) { if
	 * (inputValue.length != possibleValue.length) {
	 * Logger.logError("Input and expecting value arrays do not match"); return
	 * 1; }
	 * 
	 * 
	 * int returnCode = expecting(inputValue, possibleValue, expectedOutput,
	 * variableID, functionID, type); if(returnCode == 1) return 1;
	 * 
	 * 
	 * return 0; }
	 */
	/**
	 * Log what is expected (given in the expecting* functions) and if the block
	 * of code PASSED or FAILED.
	 * 
	 * @param variableID
	 *            the ID of the variable to test for set in the expecting
	 *            functions
	 * @param expectedOutput
	 *            the message String that is expected
	 */
	public int log(String variableID, Object actualOutput) {

		ArrayList<Output> expectedOutputs = expectedValues.get(variableID);
		String functionID = variableResidences.get(variableID);

		for (Output i : expectedOutputs) {

			if (i.getInputType().isArray()) {

				Object givenValArr = givenValues.get(variableID);
				Object expectedValArr = i.getPossibleInputValue();

				boolean match = true;
				for (int j = 0; j < Array.getLength(givenValArr); j++) {
					if (!i.getInputType()
							.getComponentType()
							.cast(Array.get(givenValArr, j))
							.equals(i.getInputType().getComponentType()
									.cast(Array.get(expectedValArr, j)))) {
						match = false;
						break;
					}
				}
				// Given and expected values have a match
				if (match) {
					if (!i.getOutputType().isArray()) {
						// Give a string representation of the values
						StringBuilder inputValue = new StringBuilder();
						inputValue.append("[");
						for (int j = 0; j < Array.getLength(givenValArr); j++)
							inputValue.append(i.getInputType()
									.getComponentType()
									.cast(Array.get(givenValArr, j)).toString()
									+ " ");
						inputValue.append("]");

						// Check if the expected objects match
						if (i.getExpectedOutput().equals(actualOutput)) {
							logInternal(
									true,
									inputValue.toString(),
									i.getOutputType().cast(actualOutput)
											.toString(),
									i.getOutputType()
											.cast(i.getExpectedOutput())
											.toString(), variableID, functionID);
							return 0;
						} else {
							logInternal(
									false,
									inputValue.toString(),
									i.getOutputType().cast(actualOutput)
											.toString(),
									i.getOutputType()
											.cast(i.getExpectedOutput())
											.toString(), variableID, functionID);
							return 1;
						}
					} else {
						// Give a string representation of the values
						StringBuilder inputValue = new StringBuilder();
						inputValue.append("[");
						for (int j = 0; j < Array.getLength(givenValArr); j++)
							inputValue.append(i.getInputType()
									.getComponentType()
									.cast(Array.get(givenValArr, j)).toString()
									+ " ");
						inputValue.append("]");

						StringBuilder actualOutputString = new StringBuilder();
						actualOutputString.append("[");
						for (int j = 0; j < Array.getLength(actualOutput); j++)
							actualOutputString.append(i.getInputType()
									.getComponentType()
									.cast(Array.get(actualOutput, j))
									.toString()
									+ " ");
						actualOutputString.append("]");

						StringBuilder expectedOutputString = new StringBuilder();
						expectedOutputString.append("[");
						for (int j = 0; j < Array.getLength(i
								.getExpectedOutput()); j++)
							expectedOutputString.append(i.getInputType()
									.getComponentType()
									.cast(Array.get(i.getExpectedOutput(), j))
									.toString()
									+ " ");
						expectedOutputString.append("]");
						boolean test = true;
						// Check if the expected objects match
						for (int j = 0; j < Array.getLength(i
								.getExpectedOutput()); j++) {

							if (!i.getOutputType()
									.getComponentType()
									.cast(Array.get(i.getExpectedOutput(), j))
									.equals(i.getOutputType()
											.getComponentType()
											.cast(Array.get(actualOutput, j)))) {
								test = false;
								break;
							}
						}

						if (test) {
							logInternal(true, inputValue.toString(),
									actualOutputString.toString(),
									expectedOutputString.toString(),
									variableID, functionID);
							return 0;
						} else {
							logInternal(false, inputValue.toString(),
									actualOutputString.toString(),
									expectedOutputString.toString(),
									variableID, functionID);
							return 1;
						}
					}

				}
			} else {
				// load the givenValues and expectedValues to see if they match
				Object givenVal = givenValues.get(variableID);
				Object expectedVal = i.getPossibleInputValue();

				boolean match = true;

				if (!i.getInputType().cast(givenVal)
						.equals(i.getInputType().cast(expectedVal)))
					match = false;

				// Given and expected values have a match
				if (match) {
					// This is where I changed alot fo code.
					// So instead of comparing strings, I am comparing objects,
					// hence the i.getOutputType().cast(actualOutput).toString
					// method call.
					// If the actual output and the expected output are the
					// same, then you have a PASS.
					// Otherwise, its a fail.
					if (!i.getOutputType().isArray()) {
						if (i.getExpectedOutput().equals(actualOutput)) {
							logInternal(true, i.getInputType().cast(givenVal)
									.toString(),
									i.getOutputType().cast(actualOutput)
											.toString(), i.getOutputType()
											.cast(i.getExpectedOutput())
											.toString(), variableID, functionID);
							return 0;
						} else {
							logInternal(false, i.getInputType().cast(givenVal)
									.toString(),
									i.getOutputType().cast(actualOutput)
											.toString(), i.getOutputType()
											.cast(i.getExpectedOutput())
											.toString(), variableID, functionID);
							return 1;
						}
					} else {
						boolean match1 = true;
						boolean test = true;
						StringBuilder actualOutputString = new StringBuilder();
						actualOutputString.append("[");
						for (int j = 0; j < Array.getLength(actualOutput); j++)
							actualOutputString.append(i.getInputType()
									.getComponentType()
									.cast(Array.get(actualOutput, j))
									.toString()
									+ " ");
						actualOutputString.append("]");

						StringBuilder expectedOutputString = new StringBuilder();
						
						expectedOutputString.append("[");
						for (int j = 0; j < Array.getLength(i
								.getExpectedOutput()); j++)
							expectedOutputString.append(i.getInputType()
									.getComponentType()
									.cast(Array.get(i.getExpectedOutput(), j))
									.toString()
									+ " ");
						expectedOutputString.append("]");
						for (int j = 0; j < Array.getLength(i
								.getExpectedOutput()); j++) {

							if (!i.getOutputType()
									.getComponentType()
									.cast(Array.get(i.getExpectedOutput(), j))
									.equals(i.getOutputType()
											.getComponentType()
											.cast(Array.get(actualOutput, j)))) {
								test = false;
								break;
							}
						}

						if (test) {
							logInternal(true, i.getInputType().cast(givenVal)
									.toString(), actualOutputString.toString(),
									expectedOutputString.toString(),
									variableID, functionID);
							return 0;
						} else {
							logInternal(false, i.getInputType().cast(givenVal)
									.toString(),
									i.getOutputType().cast(actualOutput)
											.toString(), i.getOutputType()
											.cast(i.getExpectedOutput())
											.toString(), variableID, functionID);
							return 1;
						}

					}
				}
			}
		}

		// TODO: Implement piping to any OutputStream
		return -1;
	}

	private void logInternal(boolean passed, String inputVal, String inputStr,
			String expectedStr, String variableID, String functionID) {
		Logger.logInfo(String
				.format("Variable %s %s in function %s with value %s, given %s expecting %s",
						variableID, passed ? "PASSED" : "FAILED", functionID,
						inputVal, inputStr, expectedStr));
	}

	/**
	 * Enable the builtin tester. Any functions called with the builtin tester
	 * will register after it is enabled.
	 */
	public void enable() {
		setEnabled(true);
	}

	/**
	 * Disable the builtin tester. Any functions called with the builtin tester
	 * will be ignored after it is disabled.
	 */
	public void disable() {
		setEnabled(false);
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	private void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}

class Output {
	private final Object possibleInputValue;
	private final Object expectedOutput;
	private final Class<?> inputType;
	private final Class<?> outputType;

	public Output(Object ev, Object es, Class<?> inputType, Class<?> outputType) {
		this.possibleInputValue = ev;
		this.expectedOutput = es;
		this.inputType = inputType;
		this.outputType = outputType;
	}

	public Object getPossibleInputValue() {
		return possibleInputValue;
	}

	public Object getExpectedOutput() {
		return expectedOutput;
	}

	public Class<?> getOutputType() {
		return outputType;

	}

	public Class<?> getInputType() {
		return inputType;
	}

	@Override
	public boolean equals(Object other) {
		return other == this
				|| ((other instanceof Output)
						&& getInputType().cast(
								((Output) other).possibleInputValue).equals(
								getInputType().cast(possibleInputValue)) && ((Output) other).expectedOutput
							.equals(expectedOutput));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expectedOutput == null) ? 0 : expectedOutput.hashCode());
		result = prime
				* result
				+ ((possibleInputValue == null) ? 0 : possibleInputValue
						.hashCode());
		result = prime * result
				+ ((inputType == null) ? 0 : inputType.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Output [possibleInputValue=" + possibleInputValue
				+ ", expectedOutput=" + expectedOutput + ", inputType="
				+ inputType + "]";
	}
}
