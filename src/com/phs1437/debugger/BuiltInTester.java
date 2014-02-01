/**
 * @author Jonathan Ni, Diwakar Ganesan, Kent Ma
 */

package com.phs1437.debugger;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

	private boolean isEnabled, throwsException;
    private static boolean globalIsEnabled;

	private HashMap<String, ArrayList<Output>> expectedValues = new HashMap<String, ArrayList<Output>>();
	// Mapping a input id to a value
	private HashMap<String, Object> givenValues = new HashMap<String, Object>();
	private HashMap<String, String> variableResidences = new HashMap<String, String>();
	private HashMap<String, Long> profileTimes = new HashMap<String, Long>();

	@SuppressWarnings("unchecked")
	private static final List<Class<? extends Serializable>> PRIMITIVE = Arrays
			.asList(int.class, float.class, double.class, boolean.class,
					float.class, byte.class, long.class, short.class,
					char.class, int[].class, float[].class, double[].class,
					boolean[].class, float[].class, byte[].class, long[].class,
					short[].class, char[].class);

	/**
	 * Default constructor. Options enabled: false
	 */

	public BuiltInTester()
	{
		this(false);
	}

	/**
	 * Constructor with one argument. Sets if the tester is enabled.
	 * 
	 * Options enabled: choice
	 */

	public BuiltInTester(boolean enable)
	{
		this(enable, false);
	}

	/**
	 * Constructor with two arguments. Sets if the tester is enabled and if it
	 * throws exceptions when encountering an error.
	 * 
	 * Options enabled: choice, choice
	 */

	public BuiltInTester(boolean enable, boolean throwsException)
	{
		if (enable) {
            globalEnable();
			enable();
        } else {
            globalDisable();
			disable();
        }
		setThrowsException(throwsException);
	}

	/**
	 * 
	 * Start profiling the code. Record the start time of the profile.
	 * 
	 * @param codeID
	 *            the ID associated with the segment of code being tested.
	 * @return the current time in nanoseconds.
	 */

	public long startProfile(String codeID)
	{
		long nanoTime = System.nanoTime();
		profileTimes.put(codeID, nanoTime);
		return nanoTime;
	}

	/**
	 * 
	 * Record how much time it took in nanoseconds for the current code profile.
	 * 
	 * @param codeID
	 *            the ID associated with the segment of code being tested.
	 * @return the time in nanoseconds that it took for the segment of code
	 *         being tested to execute, or -1 if failure if not throwing
	 *         exception.
	 * @throws IllegalArgumentException
	 *             if failure and throwing exception.
	 */

	public long lapProfile(String codeID)
	{
		if (!profileTimes.containsKey(codeID))
		{
			if (!throwsException())
			{
				Logger.logError("Profiler does not contain codeID");
				return -1;
			} else
				throw new IllegalArgumentException(
						"Profiler does not contain codeID");
		}

		return System.nanoTime() - profileTimes.get(codeID);
	}

	/**
	 * 
	 * Stop profiling the code portion indicated. Deletes the entry from the
	 * profile logging system.
	 * 
	 * @param codeID
	 *            the ID associated with the segment of code being tested.
	 * @return the time in nanoseconds that it took for the segment of code
	 *         being tested to execute, or -1 if failure if not throwing
	 *         exception.
	 * @throws IllegalArgumentException
	 *             if failure and throwing exception.
	 */

	public long stopProfile(String codeID)
	{
		long time = lapProfile(codeID);
		profileTimes.remove(codeID);
		return time;
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
	 *            tested is called x, this parameter's value should be "x".
	 * @param functionID
	 *            A unique string to identify the function the tested code
	 *            resided in.
	 * @param inputType
	 *            The data type of the variable to be tested. Accepts array
	 *            types.
	 * @param outputType
	 *            The data type of the output. Accepts array types.
	 * @return 0 on success, 1 on failure if not throwing exception, 2 on not
	 *         enabled.
	 * @throws IllegalArgumentException
	 *             if failure and throwing exception.
	 * 
	 */

	public int expecting(Object inputValue, Object possibleValue,
			Object expectedOutput, String variableID, String functionID,
			Class<?> inputType, Class<?> outputType)
			throws IllegalArgumentException
	{
		if (!isEnabled())
			return 2;
		// Put the given variable value assigned to a variableID

		if (PRIMITIVE.contains(inputType) || PRIMITIVE.contains(outputType))
		{
			if (!throwsException())
			{
				Logger.logError("Primitive type passed instead of Object type");
				return 1;
			} else
				throw new IllegalArgumentException(
						"Primitive type passed instead of Object type");
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

	private static boolean arrayEquals(Object array1, Object array2,
			Class<?> type)
	{

		// Both array have to have the same dimensions
		if (!array1.getClass().isArray())
		{

			return type.cast(array1).equals(type.cast(array2));

		}

		for (int i = 0; i < Array.getLength(array1); i++)
		{
			// recursively check for equality
			if (!arrayEquals(Array.get(array1, i), Array.get(array2, i), type))
				return false;

		}

		return true;

	}

	private static String printArray(Object obj, String final_str)
	{
		// if obj is not an array return
		if (obj == null || !obj.getClass().isArray())
			return obj.toString();
		int length = Array.getLength(obj); // get the length of the array

		int i = 0;

		for (i = 0; i < length; i++)
		{

			Object o = Array.get(obj, i); // get the ith element
			if (o.getClass().isArray())
				final_str += "[";
			if (!o.getClass().isArray())
			{

				final_str += o;
				if (i != length - 1)
					final_str += " ";
				// System.out.println(final_str);
			} else
			{

				final_str = printArray(o, final_str);
			}
			if (o.getClass().isArray())
				final_str += "]";

		}

		return final_str;
	}

	/**
	 * Log what is expected (given in the expecting* functions) and if the block
	 * of code PASSED or FAILED.
	 * 
	 * @param variableID
	 *            the ID of the variable to test for set in the expecting
	 *            functions.
	 * @param actualOutput
	 *            The value that the user logs into the system.
	 * 
	 * @return 0 if success, 1 if error if not throwing exception, 2 if not
	 *         enabled, -1 if logged variable not found.
	 * @throws IllegalArgumentException
	 *             if error and throwing exception.
	 */
	public int log(String variableID, Object actualOutput)
	{
		if (!isEnabled())
			return 2;

		if (!expectedValues.containsKey(variableID))
		{
			if (!throwsException())
			{
				Logger.logError("Trying to log variableID that was not added with expecting function");
				return 1;
			} else
				throw new IllegalArgumentException(
						"Trying to log variableID that was not added with expecting function");
		}

		/*
		 * The list of all Outputs associated with variableID. One Output
		 * includes a possibleValue, and an output associated with that input.
		 * Also includes the type of variable for both values.
		 */

		ArrayList<Output> expectedOutputs = expectedValues.get(variableID);
		String functionID = variableResidences.get(variableID);

		// Iterate through all the given outputs trying to find a match with the
		// log
		for (Output i : expectedOutputs)
		{

			// Fetch the current value of the variable.
			Object givenValArr = givenValues.get(variableID);

			// Fetch one of the possible values for that variable.
			Object possibleInputValArr = i.getPossibleInputValue();
			Class<?> inputComponentType = i.getInputType();

			while (inputComponentType.isArray())
			{
				inputComponentType = inputComponentType.getComponentType();

			}

			Class<?> outputComponentType = i.getOutputType();

			while (outputComponentType.isArray())
			{
				outputComponentType = outputComponentType.getComponentType();

			}

			boolean match = true;

			// check if they are equal
			match = arrayEquals(givenValArr, possibleInputValArr,
					inputComponentType);

			if (match)
			{

				String tempString = "";
				StringBuilder inputValue = new StringBuilder();
				inputValue.append("[");
				tempString = printArray(givenValArr, "");
				System.out.println("-----");
				inputValue.append(tempString);
				inputValue.append("]");

				StringBuilder actualOutputString = new StringBuilder();
				actualOutputString.append("[");
				tempString = printArray(actualOutput, "");
				actualOutputString.append(tempString);
				actualOutputString.append("]");

				StringBuilder expectedOutputString = new StringBuilder();
				expectedOutputString.append("[");
				tempString = printArray(i.getExpectedOutput(), "");
				expectedOutputString.append(tempString);
				expectedOutputString.append("]");

				boolean test = true;

				test = arrayEquals(i.getExpectedOutput(), actualOutput,
						outputComponentType);

				if (test)
				{

					logInternal(true, inputValue.toString(),
							actualOutputString.toString(),
							expectedOutputString.toString(), variableID,
							functionID);
					return 0;
				} else
				{
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
			String expectedStr, String variableID, String functionID)
	{
		Logger.logInfo(String
				.format("Variable %s %s in function %s with value %s. Logged Output: %s --- Expected Output: %s",
						variableID, passed ? "PASSED" : "FAILED", functionID,
						inputVal, inputStr, expectedStr));
	}

	/**
	 * Enable the builtin tester locally. Any functions called with the builtin 
     * tester for this instance will register after it is enabled unless the 
     * tester is globally disabled with {@link globalDisable}
	 */
	public void enable()
	{
		setEnabled(true);
	}

	/**
	 * Disable the builtin tester. Any functions called with the builtin tester
	 * object will be ignored after it is disabled.
     *
     * NOTE: This does not completely disable BuiltInTester: for that, see 
     * {@link globalDisable}
	 */
	public void disable()
	{
		setEnabled(false);
	}

    /**
     * Globally enable all instances of the Built-In Tester. All functions 
     * called within the builtin tester for all instances also enabled with 
     * {@link enable} will register.
     */
    public static void globalEnable()
    {
        globalIsEnabled = true;
    
    }
    /**
     * Globally disables all instances of the Built-In Tester.
     * Any functions called with the builtin tester object will be ignored 
     * after it is disabled.
     *
     * To disable it on only a single instance, see {@link disable}.
     */
    public static void globalDisable() 
    {
        globalIsEnabled = false;
    }

	/**
	 * @return whether the builtin tester is enabled.
	 */
	public boolean isEnabled()
	{
		return isEnabled && globalIsEnabled;
	}

	/**
	 * Sets the local state of the builtin tester
	 * 
	 * @param isEnabled
	 *            which state to set the builtin tester to
	 */
	private void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public boolean throwsException()
	{
		return throwsException;
	}

	public void setThrowsException(boolean throwsException)
	{
		this.throwsException = throwsException;
	}
}

/**
 * Used by {@link log} for the organization of data.
 */
class Output
{
	private final Object possibleInputValue;
	private final Object expectedOutput;
	private final Class<?> inputType;
	private final Class<?> outputType;

	public Output(Object ev, Object es, Class<?> inputType, Class<?> outputType)
	{
		this.possibleInputValue = ev;
		this.expectedOutput = es;
		this.inputType = inputType;
		this.outputType = outputType;
	}

	/**
	 * @return possible value saved by {@link expecting}
	 */
	public Object getPossibleInputValue()
	{
		return possibleInputValue;
	}

	/**
	 * @return expected output saved by {@link expecting}
	 */
	public Object getExpectedOutput()
	{
		return expectedOutput;
	}

	/**
	 * @return output type saved by {@link expecting}
	 */
	public Class<?> getOutputType()
	{
		return outputType;
	}

	/**
	 * @return input type saved by {@link expecting}
	 */
	public Class<?> getInputType()
	{
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
	public boolean equals(Object other)
	{
		return other == this
				|| ((other instanceof Output)
						&& getInputType().cast(
								((Output) other).possibleInputValue).equals(
								getInputType().cast(possibleInputValue)) && ((Output) other).expectedOutput
							.equals(expectedOutput));
	}

	@Override
	public int hashCode()
	{
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
	public String toString()
	{
		return "Output [possibleInputValue=" + possibleInputValue
				+ ", expectedOutput=" + expectedOutput + ", inputType="
				+ inputType + "]";
	}
}
