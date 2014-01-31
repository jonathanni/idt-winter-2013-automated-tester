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
	 * 
	 * @param inputValue
	 *            The value that is being tested.
	 * 
	 * @param possibleValue
	 *            A possible value for the variable that is being tested. The
	 *            tester will only function if the current value of the variable
	 *            matches one of the possible values given. It can be any type.
	 * @param expectedOutput
	 *            The output that matches with the given possibleValue For
	 *            example, if the function finds the square root of a number, if
	 *            possibleValue is 64, expectedOutput would be 8. It can be any
	 *            type.
	 * @param variableID
	 *            The name of the variable being tested. If the variable being
	 *            tested is called x, this parameter's value should be "x"
	 * @param functionID
	 *            A unique string to identify the function the tested code
	 *            resided in.
	 * 
	 * @param inputType
	 *            The data type of the variable to be tested. Accepts array
	 *            types.
	 * @param outputType
	 *            The data type of the output. Accepts array types.
	 * @return 0 on success and 1 on failure
	 * @throws IllegalArgumentException
	 * 
	 */

	public int expecting(Object inputValue, Object possibleValue,
			Object expectedOutput, String variableID, String functionID,
			Class<?> inputType, Class<?> outputType)
			throws IllegalArgumentException {
		// Put the given variable value assigned to a variableID

		if (inputType == int.class || inputType == float.class
				|| inputType == double.class || inputType == boolean.class
				|| inputType == float.class || inputType == byte.class
				|| inputType == long.class || inputType == short.class
				|| inputType == char.class || inputType == int[].class
				|| inputType == float[].class || inputType == double[].class
				|| inputType == boolean[].class || inputType == float[].class
				|| inputType == byte[].class || inputType == long[].class
				|| inputType == short[].class || inputType == char[].class) {
			throw new IllegalArgumentException(
					"Please pass object type instead of primitive type");
		}
		givenValues.put(variableID, inputValue);

		// Put the expected variable value with the expected String assigned to
		// a variableID

		ArrayList<Output> tempList;
		if ((tempList = expectedValues.get(variableID)) == null)
			tempList = new ArrayList<Output>();

		// possibleValue is all the values the input could be, so inputType is
		// the data type of possibleValue
		tempList.add(new Output(possibleValue, expectedOutput, inputType,
				outputType));

		expectedValues.put(variableID, tempList);

		// Assign a variableID to a functionID
		variableResidences.put(variableID, functionID);

		return 0;
	}

	/**
	 * Used by {@link log} to check if multidimensional arrays are equal
	 * 
	 * @param array1
	 *            First array to compare
	 * @param array2
	 *            Second array to compare
	 * @return true if equal, false if not
	 */

	public static boolean arrayEquals(Object array1, Object array2,
			Class<?> type) {

		// Both array have to have the same dimensions
		if (!array1.getClass().isArray()) {
			
			return type.cast(array1).equals(type.cast(array2));

		}

		for (int i = 0; i < Array.getLength(array1); i++) {
			// recursively check for equality
			if (!arrayEquals(Array.get(array1, i), Array.get(array2, i), type))
				return false;

		}

		return true;

	}

	public static String printArray(Object array1,Class<?> type, String final_str) {

		
		// Both array have to have the same dimensions
		if (!array1.getClass().isArray()) {
			System.out.println(array1.toString());
			return array1.toString()+" ";

		}

		for (int i = 0; i < Array.getLength(array1); i++) {
			// recursively check for equality
			final_str+=printArray(Array.get(array1, i), type, final_str);
			
			
		}

		return final_str;

	}
	/**
	 * Log what is expected (given in the expecting* functions) and if the block
	 * of code PASSED or FAILED.
	 * 
	 * @param variableID
	 *            the ID of the variable to test for set in the expecting
	 *            functions
	 * @param actualOutput
	 *            The value that the user logs into the system.
	 * 
	 * @return 0 if success, 1 if error, -1 if logged variable not found
	 */
	public int log(String variableID, Object actualOutput) {
		
			/*
			 * The list of all Outputs associated with variableID. One Output
			 * includes a possibleValue, and an output associated with that
			 * input. Also includes the type of variable for both values.
			 */

			ArrayList<Output> expectedOutputs = expectedValues.get(variableID);
			String functionID = variableResidences.get(variableID);

			// Iterate through all the outputs trying to find a match with the
			// log
			for (Output i : expectedOutputs) {

				// Fetch the current value of the variable.
				Object givenValArr = givenValues.get(variableID);

				// Fetch one of the possible values for that variable.
				Object expectedValArr = i.getPossibleInputValue();
				Class<?> inputComponentType = i.getInputType();

				while (inputComponentType.isArray()) {
					inputComponentType = inputComponentType.getComponentType();

				}
				
				Class<?> outputComponentType = i.getOutputType();
				
				while (outputComponentType.isArray()) {
					outputComponentType = outputComponentType.getComponentType();

				}

				boolean match = true;

				//check if they are equal
				match = arrayEquals(givenValArr, expectedValArr, inputComponentType);

				if (match) {

					String tempString = "";
					StringBuilder inputValue = new StringBuilder();
					inputValue.append("[");
					tempString = printArray(givenValArr, inputComponentType, "");
					inputValue.append(tempString);
					inputValue.append("]");
				

					
					StringBuilder actualOutputString = new StringBuilder();
					actualOutputString.append("[");
					tempString = printArray(actualOutput, outputComponentType, "");
					actualOutputString.append(tempString);
					actualOutputString.append("]");
					
					
					StringBuilder expectedOutputString = new StringBuilder();
					expectedOutputString.append("[");
					tempString = printArray(i.getExpectedOutput(), outputComponentType, "");
					expectedOutputString.append(tempString);
					expectedOutputString.append("]");

					
					boolean test = true;
					
					test = arrayEquals(i.getExpectedOutput(), actualOutput,
							outputComponentType);
					if (test) {
						logInternal(true, inputValue.toString(),
								actualOutputString.toString(),
								expectedOutputString.toString(), variableID,
								functionID);
						return 0;
					} else {
						logInternal(false, inputValue.toString(),
								actualOutputString.toString(),
								expectedOutputString.toString(), variableID,
								functionID);
						return 1;
					}

				}

			}

			
		
		return -1;
	}

	/**
	 * Logs and stores the results of the test.
	 * 
	 * @param passed
	 *            Whether the test passed
	 * @param inputVal
	 *            the value of the input variable
	 * @param inputStr
	 *            the input string
	 * @param expectedStr
	 *            The expected string to be logged by {@link log}
	 * @param variableID
	 *            The stored variable ID. See {@link expecting}
	 * @param functionID
	 *            The stored function ID. See {@link expecting}
	 */
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

	/**
	 * @return whether the builtin tester is enabled.
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * Sets the state of the builtin tester
	 * 
	 * @param isEnabled
	 *            which state to set the builtin tester to
	 */
	private void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}

/**
 * Used by {@link log} for the organization of data.
 */
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

	/**
	 * @return possible value saved by {@link expecting}
	 */
	public Object getPossibleInputValue() {
		return possibleInputValue;
	}

	/**
	 * @return expected output saved by {@link expecting}
	 */
	public Object getExpectedOutput() {
		return expectedOutput;
	}

	/**
	 * @return output type saved by {@link expecting}
	 */
	public Class<?> getOutputType() {
		return outputType;
	}

	/**
	 * @return input type saved by {@link expecting}
	 */
	public Class<?> getInputType() {
		return inputType;
	}

	/**
	 * Used for determining the equality of two values for {@link expecting}
	 * once its logged by {@link log}.
	 * 
	 * @param other
	 *            Another value from {@link expecting}
	 */
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
